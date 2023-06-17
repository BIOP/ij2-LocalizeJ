package ch.epfl.biop.ij2command;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.measure.Calibration;
import ij.process.ImageProcessor;


public class CCD_Simulator extends DetectorSimulator {
	private boolean knownCamera=false;
	
	public static int COOLSNAPHQ=1;
	
	public CCD_Simulator(double pixelSize, int exposureTime, double quantumEff, ImageProcessor ip){
		super(pixelSize,exposureTime,quantumEff, ip);
		
	}
	public CCD_Simulator(ImagePlus imp){
		super(imp,0.7,0.0005,0.001,6,10);
	}
	public CCD_Simulator(){
		super(0.7,0.0005,0.001,6,10);
	}
	
	public CCD_Simulator(ImagePlus imp, double quant, double dark, double cic, double read, double exp){
		super(imp,quant,dark,cic,read,exp);
	}
	
	
	public CCD_Simulator(ImagePlus imp,CameraDialog cd){
		super(imp,cd.quant,cd.dark,cd.cic,cd.read,cd.exp);
		this.cd=cd;
		
	}
	
	public CCD_Simulator(ImageProcessor ip, CameraDialog cd,Calibration cal){
		super(ip,cd.quant,cd.dark,cd.cic,cd.read,cd.exp, cal);
		this.cd=cd;
	}
	
	public CCD_Simulator(ImagePlus multiThreadCalculate, CCD_Simulator ccd) {
		
	}
	public ImagePlus run(){
		
		super.setTitle("CCD Camera");
		
			this.setImageProcessor(this.PoissonNoise());
			this.setImageProcessor(this.Bimodal());
			this.setImageProcessor(this.ReadNoise());
			if (cam_imp==null) cam_imp=new ImagePlus(this.getTitle(),this.cam_ip); 
			else this.cam_imp.setProcessor(this.cam_ip);
		
		return this.getImagePlus();
	}
	
	public ImagePlus run(ImagePlus imp) {
		int slice=imp.getStackSize();
		ImageStack stack=imp.createEmptyStack();

		for (int i=1;i<=slice;i++){
			IJ.showProgress(i/slice);
			
			imp.setSliceWithoutUpdate(i);
			this.setImageProcessor(imp.getProcessor());
			

			this.run();
			stack.addSlice(this.getProcessor());
			this.reset();
//				ImagePlus test=new ImagePlus("test",stack);
//				test.show();

		}
		
		this.setImagePlus(stack);
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
