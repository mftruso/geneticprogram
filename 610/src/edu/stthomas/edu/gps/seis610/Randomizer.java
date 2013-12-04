package edu.stthomas.edu.gps.seis610;

public class Randomizer {
	
	public static String randomOperatorOrOperand(){
		int option = randomGen(0,1);
		String result;
		
		switch(option){
		case 0: 
			result = randomOperator();
			break;
		case 1:
			result = randomOperand();
			break;
		default: 
			result = "";
		}
		
		return result;
	}
	
	public static String randomOperator(){
		String operator;
		int opNum = randomGen(0,3);
		
		switch(opNum) {
		case 0 : 
			operator = "+";
			break;
		case 1 : 
			operator = "-";
			break;
		case 2 : 
			operator = "*";
			break;
		case 3 : 
			operator = "/";
			break;
		default : 
			operator = "invalid";
			break;
		}
		
		return operator;
	}
	
	public static String randomOperand(){
		int num = randomGen(0,10);
		int posNeg = randomGen(0,1);
		
		if(posNeg==1){
			num = num*(-1);
		}
		
		if(num == 10 || num == -10) {
			return "x";
		} else {
			return num+"";
		}
		
	}
	
	public static Integer randomGen(int min, int max){
		Integer rando = min + (int)(Math.random() * ((max - min) + 1));
		return rando;
	}
}
