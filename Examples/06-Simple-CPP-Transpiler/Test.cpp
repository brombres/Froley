#include <iostream>
#include <string>
using namespace std;

int main()
{
  double inches, cm, deg, rad;
  cout << "Simple Test" << endl;
  inches = 12.0;
  cm = (inches * 2.54);
  cout << inches << " inches = " << cm << " cm" << endl;
  deg = 45.0;
  rad = ((deg / 180.0) * 3.1416);
  cout << deg << " degrees = " << rad << " radians" << endl;
  return 0;
}
