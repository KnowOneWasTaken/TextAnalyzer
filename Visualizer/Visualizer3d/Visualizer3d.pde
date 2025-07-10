import java.util.List;

double[][] distances = {
{0.0,9.875521390743424,14.917201645109861,16.187107271995423,12.227957953424765,12.309469075027048,11.511859756599769,14.35348483754216,11.619927220609037},
{9.875521390743424,0.0,14.88306570150483,16.155657537357467,12.186293216914459,12.268082393323134,11.467595697613518,14.318014276253693,11.554459211722774},
{14.917201645109861,14.88306570150483,0.0,19.63419457579351,16.50763515843383,16.583187063418684,16.00006415931824,18.124620825718115,16.046864206351263},
{16.187107271995423,16.155657537357467,19.63419457579351,0.0,17.635233661456784,17.64940692968503,17.146475115434782,19.09191725508192,17.161032582972624},
{12.227957953424765,12.186293216914459,16.50763515843383,17.635233661456784,0.0,14.12451422767169,13.509333989282288,16.015675791960437,13.601533481236816},
{12.309469075027048,12.268082393323134,16.583187063418684,17.64940692968503,14.12451422767169,0.0,13.583155808551496,16.046866803228742,13.656567164668656},
{11.511859756599769,11.467595697613518,16.00006415931824,17.146475115434782,13.509333989282288,13.583155808551496,0.0,15.44350296504661,12.980827174300815},
{14.35348483754216,14.318014276253693,18.124620825718115,19.09191725508192,16.015675791960437,16.046866803228742,15.44350296504661,0.0,15.52422543413097},
{11.619927220609037,11.554459211722774,16.046864206351263,17.161032582972624,13.601533481236816,13.656567164668656,12.980827174300815,15.52422543413097,0.0}
};
double mass = 1000000;
int defaultDistance = 50;
float increasePower = 1;
float scale = 2;
List<Party> parties = new ArrayList<>();
List<PVector> minForce = new ArrayList<>();
double bestTotalForce = 2000000000;
int frames = 0;
int frameCountPerRound = 15000;
int round = 0;
int rounds = 20;
boolean play = true;
int centerParty = 4;
float rotateAngleX = 0;
float rotateAngleY = 0;
float rotateAngleZ = 0;

void setup() {
  size(1920, 1400, P3D);
  //increaseDifferences(1);
  parties.add(new Party("MLPD", mass, color(#ED0008), color(0, 255, 255)));
  parties.add(new Party("III Weg", mass, color(255, 255, 255), color(0, 0, 0)));
  parties.add(new Party("AfD", mass, color(#0489DB), color(255, 255, 0)));
  parties.add(new Party("Grüne", mass, color(#1AA037), color(255, 0, 255)));
  parties.add(new Party("CDU", mass, color(40), color(255, 255, 255)));
  parties.add(new Party("FDP", mass, color(#FFEF00), color(0, 0, 255)));
  parties.add(new Party("BSW", mass, color(180, 0, 180), color(0, 255, 0)));
  parties.add(new Party("Linke", mass, color(233, 0, 153), color(0, 255, 0)));
  parties.add(new Party("SPD", mass, color(#E3000F), color(0, 255, 255)));
  parties.get(centerParty).setPos(0, 0, 0);
  /*
  
   
   
   */
}

void draw() {
  for (int  k = 0; k < 1; k++) {
    background(0);
    double totalForce = 0;
    for (int i = 0; i < distances.length; i++) {
      if (i == centerParty) {
        //continue;
      }
      for (int j = 0; j < distances.length; j++) {
        if (i!=j) {
          float range = 2;
          PVector vectorDistance = parties.get(i).getVectorBetween(parties.get(j).getPos());
          float distance = absolute(vectorDistance);
          while (distance < 1) {
            parties.get(i).setPos((int)random(-range, range) + (int)parties.get(i).getPos().x, (int)random(-range, range) + (int)parties.get(i).getPos().y, (int)random(-range, range) + (int)parties.get(i).getPos().z);
            vectorDistance = parties.get(i).getVectorBetween(parties.get(j).getPos());
            distance = absolute(vectorDistance);
          }
          float distanceReference = (float)(distances[i][j])*defaultDistance;
          PVector force = new PVector((float)(vectorDistance.x * pow((distance-distanceReference), 3)/abs((distance-distanceReference)))/(absolute(vectorDistance)),
            (float)(vectorDistance.y * pow((distance-distanceReference), 3)/abs((distance-distanceReference)))/(absolute(vectorDistance)));
          if (play) {
            totalForce+= absolute(force);
          }
          parties.get(i).applyForce(force);
        }
      }
    }

    if (totalForce < bestTotalForce) {
      bestTotalForce = totalForce;
      minForce = new ArrayList<>();
      for (Party party : parties) {
        minForce.add(party.getPos());
      }
    }

    for (Party party : parties) {
      if (play) {
        party.move();
      }
      translate(width/2-(int)(parties.get(centerParty).getPos().x/scale), height/2-(int)(parties.get(centerParty).getPos().y/scale), (int)(parties.get(centerParty).getPos().z/scale));
      rotateX(rotateAngleX);
      rotateY(rotateAngleY);
      rotateZ(rotateAngleZ);
      party.show(0, 0, 0);
      rotateX(-rotateAngleX);
      rotateY(-rotateAngleY);
      rotateZ(-rotateAngleZ);
      translate(-(width/2-(int)(parties.get(centerParty).getPos().x/scale)), -(height/2-(int)(parties.get(centerParty).getPos().y/scale)), -((int)(parties.get(centerParty).getPos().z/scale)));
    }
    if (frames % frameCountPerRound == 0) {
      round++;
      int range = 100;
      if (round < rounds) {
        for (int i = 0; i < parties.size(); i++) {
          parties.get(i).setPos((int)random(-range, range), (int)random(-range, range), (int)random(-range, range));
          parties.get(centerParty).setPos(0, 0, 0);
        }
      } else if (round == rounds) {
        play = false;
        for (int i = 0; i < parties.size(); i++) {
          parties.get(i).setPos((int)minForce.get(i).x, (int)minForce.get(i).y, (int)minForce.get(i).z);
        }
      }
    }

    fill(255);
    textAlign(LEFT);
    text("Current force: " + totalForce, 40, 50);
    text("Best Force: " + bestTotalForce, 40, 50+30*1);
    text("Round: " + round, 40, 50+30*2);
    if (play) {
      frames++;
    }
  }
  if (keyPressed) {
    if (key == '2') {
      scale *=1.1;
      println(scale);
    }
    if (key == '1') {
      scale *= 0.9;
      println(scale);
    }
    if (key == ' ') {
      play = !play;
    }
    if (key == 'a') {
      rotateAngleY += 0.02;
      println("rotate");
    }
    if (key == 's') {
      rotateAngleX -= 0.02;
      println("rotate");
    }
    if (key == 'w') {
      rotateAngleX += 0.02;
      println("rotate");
    }
    if (key == 'd') {
      rotateAngleY -= 0.02;
      println("rotate");
    }
  }
}

float absolute(PVector vector) {
  return sqrt(sq(vector.x) + sq(vector.y));
}

public void increaseDifferences(float power) {
  double distanceTotal = 0;
  for (int i = 0; i < distances.length; i++) {
    for (int j = 0; j < distances[i].length; j++) {
      distanceTotal += distances[i][j];
    }
  }
  double normalDistance = distanceTotal / sq(distances.length);
  double[][] abweichung = new double[distances.length][distances.length];
  for (int i = 0; i < distances.length; i++) {
    for (int j = 0; j < distances[i].length; j++) {
      if (i!=j) {
        abweichung[i][j] = normalDistance - distances[i][j];
      } else {
        abweichung[i][j] = 0;
      }
    }
  }
  for (int i = 0; i < distances.length; i++) {
    for (int j = 0; j < distances[i].length; j++) {
      if (abweichung[i][j] > 0) {
        distances[i][j] = normalDistance - abs(pow((float)abweichung[i][j], power));
      } else if (abweichung[i][j] < 0) {
        distances[i][j] = normalDistance + abs(pow((float)abweichung[i][j], power));
      }
    }
  }
  for (int i = 0; i < distances.length; i++) {
    println();
    for (int j = 0; j < distances[i].length; j++) {
      print(distances[i][j]+", ");
    }
  }
}

void mouseDragged() {
  for (Party party : parties) {
    if (party.isMouseIn(width/2, height/2)) {
      party.setPos((int)(mouseX*scale-width/2*scale), (int)(mouseY*scale-height/2*scale), 0);
    }
  }
}
