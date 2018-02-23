import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        // t - tetragon
        // p - pentagon
        // h - hexagon
        // c - circle

        List<Shape> shapes = new ArrayList<>();
        int i = 1;
        try {
            for (char c : args[0].toCharArray()) {
                double[] tmp = new double[5];

                switch (c) {
                case 't': 
                    for (int j = 0; j < 5; j++) {
                        tmp[j] = Double.parseDouble(args[i + j]);
                    }
                    i += 5;
                    shapes.add(Tetragon.makeTetragon(tmp[0], tmp[1], tmp[2], tmp[3], tmp[4]));
                    break;
                case 'p':
                    tmp[0] = Double.parseDouble(args[i]);
                    i++;
                    shapes.add(new Pentagon(tmp[0]));
                    break;
                case 'h':
                    tmp[0] = Double.parseDouble(args[i]);
                    i++;
                    shapes.add(new Hexagon(tmp[0]));
                    break;
                case 'c':
                    tmp[0] = Double.parseDouble(args[i]);
                    i++;
                    shapes.add(new Circle(tmp[0]));
                    break;
                default:
                    System.out.println("'" + c + "'" + " doesn't denote any shape");
                    return;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Not every argument is a number");
        }

        for (Shape shape : shapes) {
            System.out.println(
                shape.getClass().getName() 
                + " with area " + shape.surfaceArea()
                + " and perimeter " + shape.perimeter());
        }
    }

}
                    
                    

    
    
        
    
    
    
        
    


    
    
        
    
    
    
        
    


    
    
        
    
    
    
        
    


    
    
        
    
    
    
        
    
