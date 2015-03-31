package fi.ni.ifc2x3;
import fi.ni.ifc2x3.interfaces.*;
import fi.ni.*;
import java.util.*;

/*
 * IFC Java class
 * @author Jyrki Oraskari
 * @license This work is licensed under a Creative Commons Attribution 3.0 Unported License.
 * http://creativecommons.org/licenses/by/3.0/ 
 */

public class IfcStructuralLoadSingleForceWarping extends IfcStructuralLoadSingleForce 
{
 // The property attributes
 Double warpingMoment;


 // Getters and setters of properties

 public Double getWarpingMoment() {
   return warpingMoment;
 }
 public void setWarpingMoment(String txt){
   Double value = i.toDouble(txt);
   this.warpingMoment=value;

 }

}
