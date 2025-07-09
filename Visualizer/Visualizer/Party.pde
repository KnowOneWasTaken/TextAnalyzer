class Party {
  String name;
  color col;
  int x = (int)random(-100,100);
  int y = (int)random(-100,100);
  PVector v = new PVector(0,0);
  double mass;
  color textCol;
  int size = 80;
  
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
     circle(dx + x/scale, dy + y/scale, size);
     fill(textCol);
     textSize(30);
     textAlign(CENTER);
     text(name, dx + x/scale, dy + y/scale + 10, 50);
  }
  
  boolean isMouseIn(int dx, int dy) {
    boolean inX = dx + x/scale - size < mouseX && dx + x/scale + size > mouseX;
    boolean inY = dy + y/scale - size < mouseY && dy + y/scale + size > mouseY;
    return inX && inY;
  }
  
  PVector getPos() {
   return new PVector(x, y); 
  }
  
  PVector getVectorBetween(PVector other) {
    return new PVector(other.x - x, other.y - y);
  } 
}
