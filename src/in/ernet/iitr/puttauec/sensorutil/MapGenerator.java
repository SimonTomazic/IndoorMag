package in.ernet.iitr.puttauec.sensorutil;

import org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolatingFunction;
import org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapGenerator implements Runnable{
	public int N = 4;                     // No. of measurement pathways in the Magnetic field  Experiment Setup.
	private int a = 2;                    // The instance specific magnetic field axes ID.  0 - x , 1 - y, 2- z
	private String json_obj,name;			
	public double [][] magnitudes;		  // Z = f(x,y) where Z is Magnetic field input for The interpolation function
	public double [] xs;                  // x-axes measurement co-ordinates
	public double [] ys = new double[51]; // y-axes measurement co-ordinates
	
	public BicubicSplineInterpolatingFunction f;   // Interpolation function for the estimation of magnetic field at any position (x,y)
	public BicubicSplineInterpolator interpolator = new BicubicSplineInterpolator();	
    private int j,k,len;
 	    	
	public MapGenerator(String json, int Nval, int k)
	{   // System.out.println("Map");
		 N = Nval;
		 a = k;
	     json_obj = json;
	    
	}	
	
	/** Loads the Magnetic field readings from the JSON object to the 2-D array. 	   
	 * @param the_json,  String JSON object
	 */
	public void parseProfilesJson (String the_json) {
		 try {	 JSONObject myjson   = new JSONObject(the_json);
		         JSONArray nameArray = myjson.names();
		         len = nameArray.length();
		         for(int i = 0; i < len; i++)
		         	{  name = nameArray.getString(i);
		        	   JSONArray json_array  = myjson.getJSONArray(name);
		        	   j = (Integer.valueOf(name)-1) / 51;
		        	   k = (Integer.valueOf(name)-1) % 51;
		        	   if(j >= 6 && j <= 8)
		        	   		{ j -=3;
		        	   		}
		        	   else if(j >= 12 && j <=15)
		        	   		{ j -= 6;		        		   
		        	   		}
    	              magnitudes[j][k] = json_array.getDouble(a);
		            //hard coded form of data input from the JSON 3rd column
		      	     }    
		          for(int i = 0; i < 51; i++)
		            { ys[i] = i;
		      	    }    
		          if(N != 10)
		            {  for(int i=0; i < N; i++)
		                   { xs[i] = i;
		      	           } 
		            }
		          else
		            {   xs[0] = 0; xs[1] =1; xs[2]=2;
		          	    xs[3] = 6; xs[4] =7; xs[5]=8;
		                xs[6] = 12; xs[7] =13; xs[8]=14; xs[9]=15;
		            }
		       } catch (JSONException e) {
		                e.printStackTrace();
		       }		      
		   }
	
	/**  Generate Interpolation Function for the instance which will be used in the particle filter for estimating the particles 
	 *  Magnetic field at some location (x,y) 
	 */
	public void run()
	{    android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
	     magnitudes = new double[N][51];
    	 xs = new double[N];
    	 parseProfilesJson(json_obj);		    
    	 f = interpolator.interpolate(xs, ys, magnitudes);
    	 magnitudes = null;  
	}
}
	
		  		

