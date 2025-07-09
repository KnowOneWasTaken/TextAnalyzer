import java.util.List;

double[][] distances = {  {0, 1, 2, 3, 4},
                          {1, 0, 2, 0.5, 6},
                          {2, 2, 0, 1, 3},
                          {3, 0.5, 1, 0, 3},
                          {4, 6, 3, 3, 0}};;
double mass = 1;
int defaultDistance = 150;
List<Party> parties = new ArrayList<>();

void setup() {
  size(1400,1400);
  //for (int i = 0; i < distances.length; i++) {
  //  for (int j = 0; j < distances[0].length; j++) {
  //     distances[i][j] = random(0,1); 
  //  }
  //}

  
  parties.add(new Party("Grüne", mass, color(0,255,0), color(255,0,255)));
  parties.get(0).setPos(0,0);
  parties.add(new Party("CDU", mass, color(0,0,0), color(255,255,255)));
  parties.add(new Party("FDP", mass, color(255,255,0), color(0,0,255)));
  parties.add(new Party("SPD", mass, color(255,0,0), color(0,255,255)));
  parties.add(new Party("AfD", mass, color(0,40,240), color(255,255,0)));
}

void draw() {
  background(15);
  for (int i = 1; i < distances.length; i++) {
     for (int j = 0; j < distances.length; j++) {
       if(i!=j) {
         PVector vectorDistance = parties.get(i).getVectorBetween(parties.get(j).getPos());
         float distance = absolute(vectorDistance);
         float distanceReference = (float)(distances[i][j] * defaultDistance);
         PVector force = new PVector((float)(vectorDistance.x * (distance-distanceReference))/(absolute(vectorDistance)) * 0.01, (float)(vectorDistance.y * (distance-distanceReference))/(absolute(vectorDistance)) * 0.01);
         parties.get(i).applyForce(force);
       }
    }
    PVector force = parties.get(i).getVectorBetween(new PVector(0,0));
    parties.get(i).applyForce(new PVector(force.x*0.001, force.y*0.001));
  }
  for (Party party : parties) {
    party.move();
    party.show(700,700);
  }
}

float absolute(PVector vector) {
  return sqrt(sq(vector.x) + sq(vector.y));
}
