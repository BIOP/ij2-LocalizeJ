package ch.epfl.biop.ij2command;

import java.io.File;

import org.scijava.command.Command;
import org.scijava.plugin.Parameter;
import org.scijava.plugin.Plugin;

import ij.IJ;
import ij.ImagePlus;
import net.imagej.ImageJ;

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
    @Parameter(label="photoflux in photons/molecule")
    int photons;
    
    @Parameter(label="# of molecules")
    int numMol;
    
    @Parameter(label="# of frames")
    int frames;
    
    @Parameter(label="multithreading")
    boolean multithread;
    
    @Parameter (choices={"CCD camera", "EM CCD camera","sCMOS camera"}, style="listBox") 
    String camera;
    
    @Override
    public void run() {
    	
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
		
		if (camera.equals(DetectorSimulator.type[0])){
			CCD_Simulator ccd=new CCD_Simulator();
			if (multithread) ccd=new CCD_Simulator(dg.multiThreadCalculate(),ccd);
			else ccd.run(dg.calculate()).show();
				
/*			if (saveBlink){
				IJ.save(ccd.run(),blinkName);
			} else */ ccd.run().show();
		}
		
		if (camera.equals(DetectorSimulator.type[2])){
			EMCCD_Simulator emccd=new EMCCD_Simulator();
			if (multithread) emccd=new EMCCD_Simulator(dg.multiThreadCalculate(),emccd);
			else emccd.run(dg.calculate()).show();	
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


