package ch.epfl.biop.ij2command;

	import ij.gui.GenericDialog;
	

	public class PMTDialog {
		
		GenericDialog gd;
		double photon=200;
		double quant=0.2;
		boolean load=false;
		boolean norm=false;
			
		PMTDialog(){
			
			gd=new GenericDialog("PMT Parameters");
			gd.addNumericField("PhotonFlux", photon,0);
			gd.addNumericField("QE", quant, 2);
			gd.addCheckbox("Load Image", load);
			gd.addCheckbox("Normalize Image", norm);
		}
		
		public void showDialog() {
			
			gd.showDialog();
			if (gd.wasCanceled()) return;
			
			photon=gd.getNextNumber();
			quant=gd.getNextNumber();
			load=gd.getNextBoolean();
			norm=gd.getNextBoolean();
			
				
		}
		
		public void setLoad(boolean b){
			this.load=b;
		}

	}


