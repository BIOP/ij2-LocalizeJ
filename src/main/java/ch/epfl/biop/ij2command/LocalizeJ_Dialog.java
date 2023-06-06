package ch.epfl.biop.ij2command;

import java.io.File;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;
import net.imagej.ImageJ;
import ij.IJ;

import ij.ImagePlus;

/**
 * This example illustrates how to create an ImageJ 2 {@link Command} plugin.
 * The pom file of this project is customized for the PTBIOP Organization (biop.epfl.ch)
 * <p>
 * The code here is opening the biop website. The command can be tested in the java DummyCommandTest class.
 * </p>
 */

@Plugin(type = Command.class, menuPath = "Plugins>BIOP>LocalizeJ")
public class LocalizeJ_Dialog implements Command {

	@Parameter(style="open", label="Pattern")
    File pattern;
	
	@Parameter(style="open", label="PSF")
    File psf;
	
/*	@Parameter
    int width;

    @Parameter
    int height;
*/    
    @Parameter(label="k on")
    double kon;
    
    @Parameter(label="k off")
    double koff;
    
    @Parameter(label="bleaching rate")
    double bleach;
    
//    @Parameter
//    int band;
    @Parameter(label="number of photons/molecule")
    int photons;
    
    @Parameter(label="number of molecules")
    int numMol;
    
    @Parameter(label="number of frames")
    int frames;
    
    @Override
    public void run() {
    	
    	IJ.log("Please specify the Camera Parameters");
		CameraDialog cd=new CameraDialog();
		cd.showDialog();
		
    	TimeGenerator time=new TimeGenerator(1024,1024);
		time.setParticleNumber(numMol);
		time.setFrameNumber(frames);
		time.setOnRate(kon);
		time.setOffRate(koff);
		time.setBleachRate(bleach); 
		time.generateMask(pattern.getAbsolutePath());
    	
		time.heavyTimeTrace();
/*		if (saveTime){
			IJ.save(new ImagePlus("TimeTrace condensed",time.condensedTimeTrace()),timeName);
		}
		
		if (project) time.getProjectedTimeTrace().show();
*/		
		ImagePlus imp=IJ.openImage(psf.getAbsolutePath());
//		IJ.open(psf.getAbsolutePath());
//		ImagePlus imp=WindowManager.getCurrentImage();
		
		DiffractionGenerator dg=new DiffractionGenerator(time,photons,imp.getProcessor(),new DiffractionDialog());
//		dg.multiThreadCalculate().show();
		
		if (cd.choice.equals(DetectorSimulator.type[0])){
//			CCD_Simulator ccd=new CCD_Simulator(dg.multiThreadCalculate(),cd);
			CCD_Simulator ccd=new CCD_Simulator(dg.calculate(),cd);
				
/*			if (saveBlink){
				IJ.save(ccd.run(),blinkName);
			} else */ ccd.run().show();
		}
		
		if (cd.choice.equals(DetectorSimulator.type[2])){
//			EMCCD_Simulator emccd=new EMCCD_Simulator(dg.multiThreadCalculate(),cd);
			EMCCD_Simulator emccd=new EMCCD_Simulator(dg.calculate(),cd);	
/*			if (saveBlink){
				IJ.save(emccd.run(),blinkName);
			} else */ emccd.run().show();
		}
    	    	
    	
    }

    /**
     * This main function serves for development purposes.
     * It allows you to run the plugin immediately out of
     * your integrated development environment (IDE).
     *
     * @param args whatever, it's ignored
     * @throws Exception
     */
    public static void main(final String... args) throws Exception {
        // create the ImageJ application context with all available services
        final ImageJ ij = new ImageJ();
        ij.ui().showUI();

        ij.command().run(LocalizeJ_Dialog.class, true);
    }
}


