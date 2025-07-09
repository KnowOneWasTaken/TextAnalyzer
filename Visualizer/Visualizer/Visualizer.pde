import java.util.List;

double[][] distances = {
  {0.0, 19.432544397614794, 19.070796805575707, 18.344747016477683, 19.852314455915067, 1.5499554341799515, 3.6834983840605484, 3.619524426987866, 11.473335637620577},
  {19.432544397614794, 0.0, 21.291638730324763, 20.302112237448572, 20.977431910593605, 25.69096133963491, 29.86275867723236, 36.93082416779364, 42.6505048031075},
  {19.070796805575707, 21.291638730324763, 0.0, 23.417802432999217, 22.241117865509448, 25.440841966126534, 24.642720101852515, 27.03479662789076, 26.857234509268864},
  {18.344747016477683, 20.302112237448572, 23.417802432999217, 0.0, 21.548402294608724, 18.898360189787812, 18.904902777945125, 12.08989740234257, 17.824081883932966},
  {19.852314455915067, 20.977431910593605, 22.241117865509448, 21.548402294608724, 0.0, 23.25147429399139, 18.073374728572087, 20.895024514802063, 29.8411569059644},
  {1.5499554341799515, 25.69096133963491, 25.440841966126534, 18.898360189787812, 23.25147429399139, 0.0, 22.645574325457964, 27.870198385882862, 33.26891842659323},
  {3.6834983840605484, 29.86275867723236, 24.642720101852515, 18.904902777945125, 18.073374728572087, 22.645574325457964, 0.0, 26.806315007150655, 24.933135053404925},
  {3.619524426987866, 36.93082416779364, 27.03479662789076, 12.08989740234257, 20.895024514802063, 27.870198385882862, 26.806315007150655, 0.0, 0.30487596347619617},
  {11.473335637620577, 42.6505048031075, 26.857234509268864, 17.824081883932966, 29.8411569059644, 33.26891842659323, 24.933135053404925, 0.30487596347619617, 0.0}
};
double mass = 100;
int defaultDistance = 50;
float increasePower = 1;
float scale = 2;
List<Party> parties = new ArrayList<>();
List<PVector> minForce = new ArrayList<>();
double bestTotalForce = 2000000000;
int frames = 0;
int frameCountPerRound = 800;
int round = 0;
int rounds = 100;
boolean play = true;

void setup() {
  size(1400, 1400);
  increaseDifferences(1);
  parties.add(new Party("MLPD", mass, color(220, 10, 10), color(0, 255, 255)));
  parties.add(new Party("III Weg", mass, color(255, 255, 255), color(0, 0, 0)));
  parties.add(new Party("AfD", mass, color(0, 40, 240), color(255, 255, 0)));
  parties.add(new Party("Grüne", mass, color(0, 255, 0), color(255, 0, 255)));
  parties.add(new Party("CDU", mass, color(0, 0, 0), color(255, 255, 255)));
  parties.add(new Party("FDP", mass, color(255, 255, 0), color(0, 0, 255)));
  parties.add(new Party("BSW", mass, color(180, 0, 180), color(0, 255, 0)));
  parties.add(new Party("Linke", mass, color(255, 0, 255), color(0, 255, 0)));
  parties.add(new Party("SPD", mass, color(255, 0, 0), color(0, 255, 255)));
  parties.get(0).setPos(0, 0);
  /*
  
   
   
   */
}

void draw() {
  for (int  k = 0; k < 60; k++) {
    background(15);
    double totalForce = 0;
    for (int i = 1; i < distances.length; i++) {
      for (int j = 0; j < distances.length; j++) {
        if (i!=j) {
          PVector vectorDistance = parties.get(i).getVectorBetween(parties.get(j).getPos());
          float distance = absolute(vectorDistance);
          if (distance <= 0.0001) {
            vectorDistance = new PVector(0.1, 0.1);
            distance = absolute(vectorDistance);
          }
          float distanceReference = (float)(distances[i][j] * defaultDistance);
          PVector force = new PVector((float)(vectorDistance.x * (distance-distanceReference))/(absolute(vectorDistance)) * 0.01, (float)(vectorDistance.y * (distance-distanceReference))/(absolute(vectorDistance)) * 0.01);
          parties.get(i).applyForce(force);
        }
      }
      PVector force = parties.get(i).getVectorBetween(new PVector(0, 0));
      totalForce += absolute(force);
      if (play) {
        parties.get(i).applyForce(new PVector(force.x*0.001, force.y*0.001));
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
      party.show(700, 700);
    }
    if (frames % frameCountPerRound == 0) {
      round++;
      int range = 10;
      if (round < rounds) {
        for (int i = 0; i < parties.size(); i++) {
          parties.get(i).setPos((int)random(-range, range), (int)random(-range, range));
        }
      } else if (round == rounds) {
        for (int i = 0; i < parties.size(); i++) {
          parties.get(i).setPos((int)minForce.get(i).x, (int)minForce.get(i).y);
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

void keyPressed() {
  if (key == 'w') {
    increasePower += 1;
    println(increasePower);
  }
  if (key == 's') {
    increasePower -= 1;
    println(increasePower);
  }
  if (key == ' ') {
    play = !play;
  }
}

void mouseDragged() {
  for (Party party : parties) {
    if (party.isMouseIn(width/2, height/2)) {
      party.setPos((int)(mouseX*scale-width/2*scale), (int)(mouseY*scale-height/2*scale));
    }
  }
}
