package ch.epfl.biop.ij2command;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.Calibration;
import ij.process.ImageProcessor;


public class PMT_Simulator extends DetectorSimulator {
		
	public PMT_Simulator(double pixelSize, int exposureTime, double quantumEff, ImageProcessor ip){
		super(pixelSize,exposureTime,quantumEff, ip);
		
	}
	public PMT_Simulator(ImagePlus imp, double quant){
		super(imp,quant);
	}
	
	
	public ImagePlus run(){
		int slice=super.getImagePlus().getStackSize();
		super.setTitle("PMT Detector");
		if (slice==1){
			
			this.setImageProcessor(this.Bimodal());
			this.cam_imp.setProcessor(this.cam_ip);
		}
		else {
			
			ImageStack stack=this.getImagePlus().createEmptyStack();

			for (int i=1;i<=slice;i++){
				IJ.showProgress(i/slice);
				
				this.getImagePlus().setSliceWithoutUpdate(i);
//				PMT_Simulator ccd=new PMT_Simulator(this.getProcessor().duplicate(),this.cd,this.getCalibration());

//					ccd.run();
//					stack.addSlice(ccd.getProcessor());
//					ImagePlus test=new ImagePlus("test",stack);
//					test.show();

			}
			this.setImagePlus(stack);
		}
		return this.getImagePlus();
	}
	
	ImagePlus resultStack(){
		 
		 ImageStack stack=new ImageStack(super.getWidth(),super.getHeight());
		 stack.addSlice(this.getImagePlus().getProcessor().duplicate().convertToFloat());
		 stack.addSlice(this.PoissonNoise().duplicate().convertToFloat());
		 stack.addSlice(this.Bimodal().duplicate());
		 
		 return new ImagePlus("Noise stack",stack);
	 }
	
}
