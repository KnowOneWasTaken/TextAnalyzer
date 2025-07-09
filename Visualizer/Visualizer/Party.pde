class Party {
  String name;
  color col;
  int x = (int)random(-20,20);
  int y = (int)random(-20,20);
  PVector v = new PVector(0,0);
  double mass;
  color textCol;
  
  public Party(String name, double mass, color col, color textCol) {
    this.name = name;
    this.mass = mass;
    this.col = col;
    this.textCol = textCol;
  }
  
  public void applyForce(PVector force) {
    v.x += force.x / mass;
    v.y += force.y / mass;
  }
  
  public void move() {
    x += v.x;
    y += v.y;
    v.x = v.x*0.9;
    v.y = v.y*0.9;
  }
  
  public void setPos(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  void show(int dx, int dy) {
     stroke(255);
     fill(col);
     circle(dx + x, dy + y, 80);
     fill(textCol);
     textSize(30);
     textAlign(CENTER);
     text(name, dx + x, dy + y + 10, 50);
  }
  
  PVector getPos() {
   return new PVector(x, y); 
  }
  
  PVector getVectorBetween(PVector other) {
    return new PVector(other.x - x, other.y - y);
  } 
}
