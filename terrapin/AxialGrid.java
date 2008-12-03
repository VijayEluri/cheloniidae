// Terrapin Turtle System copyright 2006 by Spencer Tipping
// Written 08-02-2006; licensed under the LGPL, latest version

  package terrapin;

  import java.awt.*;
  import java.util.*;

/**
 * Add this class to a TurtleCanvas to create a grid on an axis-aligned
 * plane.
 *
 * @author Spencer Tipping
 */

  public class AxialGrid implements BackgroundObject {
    // Constants
      // These fields are used to determine on which planes the
      // grids appear.
      public static final int   XY_PLANE        = 1 << 0;
      public static final int   XZ_PLANE        = 1 << 1;
      public static final int   YZ_PLANE        = 1 << 2;

    // Fields
      // This contains a bitmask of the plane constants above. If a
      // bit is set, then the corresponding plane is drawn.
      protected int             planes          = XY_PLANE;
        
      // These variables control how large and dense the grid is.
      protected double          gridWidth       = 400.0;
      protected double          gridHeight      = 400.0;
      protected double          gridStep        = 50.0;

      // This variable specifies what color the grid is.
      protected Color           gridColor       = new Color (0.5f, 0.6f, 0.5f, 0.7f);

      // This variable specifies the thickness of the grid lines.
      protected double          gridThickness   = 0.5;

      // This variable specifies the length of the tick marks' crosshairs.
      protected double          tickSize        = 2.0;

      // This variable prevents us from having to reconstruct the list
      // during each screen refresh.
      protected LinkedList      cache           = null;

    // Constructors
      public AxialGrid () {}

      public AxialGrid (int _planes) {
        planes = _planes;
      }

    // Accessors
      public int        getPlanes ()                                    {return planes;}
      public void       setPlanes (int _planes)                         {planes = _planes;}

      public double     getGridWidth ()                                 {return gridWidth;}
      public void       setGridWidth (double _gridWidth)                {gridWidth = _gridWidth;}

      public double     getGridHeight ()                                {return gridHeight;}
      public void       setGridHeight (double _gridHeight)              {gridHeight = _gridHeight;}

      public double     getGridStep ()                                  {return gridStep;}
      public void       setGridStep (double _gridStep)                  {gridStep = _gridStep;}

      public Color      getGridColor ()                                 {return gridColor;}
      public void       setGridColor (Color _gridColor)                 {gridColor = _gridColor;}
      
      public double     getGridThickness ()                             {return gridThickness;}
      public void       setGridThickness (double _gridThickness)        {gridThickness = _gridThickness;}

      public double     getTickSize ()                                  {return tickSize;}
      public void       setTickSize (double _tickSize)                  {tickSize = _tickSize;}

    // Methods
      // Grid management (available to user)
        public void reinitializeGrid () {
          // This method should be called whenever the grid's parameters
          // are changed at runtime.
          cache = null;
        }

        public LinkedList getLines () {
          // Draw a bunch of little tick marks where the grid points
          // are located.

          if (cache != null)
            return cache;
          else {
            int totalX         = (int) (gridWidth / gridStep);
            int totalY         = (int) (gridHeight / gridStep);
            LinkedList result  = new LinkedList ();
            
            for (int i = -totalX; i <= totalX; i++)
              for (int j = -totalY; j <= totalY; j++) {
                if ((planes & XY_PLANE) != 0)
                  makeTick (i * gridStep, j * gridStep, 500.0, result);

                if ((planes & XZ_PLANE) != 0)
                  makeTick (i * gridStep, 0, j * gridStep + 500.0, result);

                if ((planes & YZ_PLANE) != 0)
                  makeTick (0, i * gridStep, j * gridStep + 500.0, result);
              }

            return cache = result;
          }
        }

      // Grid management (hidden from user)
        protected void makeTick (double x, double y, double z, LinkedList target) {                  
          // Draw three line segments forming a 3D plus.
          target.add (new Line (x + tickSize, y, z, x - tickSize, y, z, gridThickness, gridColor));
          target.add (new Line (x, y + tickSize, z, x, y - tickSize, z, gridThickness, gridColor));
          target.add (new Line (x, y, z + tickSize, x, y, z - tickSize, gridThickness, gridColor));
        }

  }
