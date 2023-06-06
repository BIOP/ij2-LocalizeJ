package ch.epfl.biop.ij2command;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.process.ImageProcessor;


public class PMT_Dialog implements PlugIn{

		
	public void run(String arg) {
		PMTDialog pd=new PMTDialog();
		
		ImagePlus imp=WindowManager.getCurrentImage();
		if (imp==null){
			pd.setLoad(true);
		}
		
		pd.showDialog();
		if (pd.load) imp=IJ.openImage();
		
		
		ImageProcessor ip=imp.getProcessor().duplicate();
		double max=ip.maxValue();
		ip=ip.convertToShort(false);
			
		if (pd.norm) ip.multiply(pd.photon/max);
		
		imp.setProcessor(ip);
		
		PMT_Simulator pmt=new PMT_Simulator(imp,pd.quant);
		imp=pmt.run();
				
/*		switch (selectCameraType(cd.choice)){
		case 0: CCD_Simulator ccd;
				if (cd.preset) ccd=new CCD_Simulator(imp);
				else ccd=new CCD_Simulator(imp,cd.quant,cd.dark,cd.cic,cd.read,cd.exp);
				imp=ccd.run();
				break;
		case 1: IJ.showMessage("...non valid choice");
				break;
		case 2: EMCCD_Simulator emccd;
				if (cd.preset) emccd=new EMCCD_Simulator(imp,cd.emGain);
				else emccd=new EMCCD_Simulator(imp,cd.quant,cd.dark,cd.cic,cd.read,cd.exp,cd.emGain);
				imp=emccd.run();
		}
*/		
	imp.show();	
	}

}

