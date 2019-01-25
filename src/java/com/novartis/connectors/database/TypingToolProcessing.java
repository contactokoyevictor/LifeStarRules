package com.novartis.connectors.database;


public class TypingToolProcessing
{
	private int countadd = 0;
	private double one = 0;
	private double two = 0;
	private double three = 0;
	private double four = 0;
	
	
	public String getQuestionName(int index)
	{
		if(index > 0 && index < 8 ){
	
		String[] QuestioNames = new String[] {"QuestionOne","QuestionTwo","QuestionThree","QuestionFour","QuestionFive","QuestionSix","QuestionSeven"};
				
		return QuestioNames[index = index -1];
		
		}
		return null;
		
	}
	
	public int getQuestionCategory(int questionName, int responsenumber)
	{
	String questionNameString = getQuestionName(questionName);	
		
	switch(questionName){
	case (1):
	if((getQuestionName(1) == questionNameString) && (responsenumber == 6 || responsenumber == 7 ))
	{
		return 0;
	}
	if((getQuestionName(1) == questionNameString) && (responsenumber >= 3 && responsenumber <= 5))
	{
		return 1;
	}
	
	if((getQuestionName(1) == questionNameString) && (responsenumber == 1 || responsenumber == 2))
	{
		return 2;
	}	
	break;
	
	case (2):
		if((getQuestionName(2) == questionNameString) && (responsenumber == 6 || responsenumber == 7 ))
		{
			return 0;
		}
		if((getQuestionName(2) == questionNameString) && (responsenumber == 4 || responsenumber == 5))
		{
			return 1;
		}
		
		if((getQuestionName(2) == questionNameString) && (responsenumber >= 1 && responsenumber <= 3))
		{
			return 2;
		}
		
	break;	
	
	case (3):
		if((getQuestionName(3) == questionNameString) && (responsenumber == 6 || responsenumber == 7 ))
		{
			return 0;
		}
		if((getQuestionName(3) == questionNameString) && (responsenumber == 4 || responsenumber == 5))
		{
			return 1;
		}
		
		if((getQuestionName(3) == questionNameString) && (responsenumber >= 1 && responsenumber <= 3))
		{
			return 2;
		}
		
	break;
	
	case (4):
		if((getQuestionName(4) == questionNameString) && (responsenumber == 6 || responsenumber == 7 ))
		{
			return 0;
		}
		if((getQuestionName(4) == questionNameString) && (responsenumber >= 3 && responsenumber <= 5))
		{
			return 1;
		}
		
		if((getQuestionName(4) == questionNameString) && (responsenumber == 1 || responsenumber == 2))
		{
			return 2;
		}	
		
	break;
	
	case (5):
		if((getQuestionName(5) == questionNameString) && (responsenumber >= 5 && responsenumber <= 7 ))
		{
			return 0;
		}
		if((getQuestionName(5) == questionNameString) && (responsenumber >= 3 && responsenumber <= 4))
		{
			return 1;
		}
		
		if((getQuestionName(5) == questionNameString) && (responsenumber == 1 || responsenumber == 2))
		{
			return 2;
		}	
		
	break;
	
	case (6):
		if((getQuestionName(6) == questionNameString) && (responsenumber >= 5 && responsenumber <= 7 ))
		{
			return 0;
		}
		if((getQuestionName(6) == questionNameString) && (responsenumber >= 3 && responsenumber <= 4))
		{
			return 1;
		}
		
		if((getQuestionName(6) == questionNameString) && (responsenumber == 1 || responsenumber == 2))
		{
			return 2;
		}	
		
	break;
	
	case (7):
		if((getQuestionName(7) == questionNameString) && (responsenumber >= 5 && responsenumber <= 7 ))
		{
			return 0;
		}
		if((getQuestionName(7) == questionNameString) && (responsenumber >= 3 && responsenumber <= 4))
		{
			return 1;
		}
		
		if((getQuestionName(7) == questionNameString) && (responsenumber == 1 || responsenumber == 2))
		{
			return 2;
		}	
		
	break;
	
	
	}
	return 111111;	
		
	}
	
		
	public Object[] getCoefficientValues(String questionName)
	{
		if(questionName == "QuestionOne"){
		Object[] QuestionOne = new Object[3];		
        QuestionOne[0] = new double[] {0.1497, 0.0901, -0.1201, -0.1359};
        QuestionOne[1] = new double[] {0.0676, 0.058, -0.0741, -0.0603};
        QuestionOne[2] = new double[] {0, 0, 0, 0};
        //generateSegmentArray(QuestionOne,categoryNumber);
        return QuestionOne;
		}
		if(questionName == "QuestionTwo"){
        Object[] QuestionTwo = new Object[3];
        QuestionTwo[0] = new double[] {0.1953, 0.059, 0.0213, -0.2866};
        QuestionTwo[1] = new double[] {0.0718, 0.0154, 0.0516, -0.1406};
        QuestionTwo[2] = new double[] {0, 0, 0, 0};
        //generateSegmentArray(QuestionTwo,categoryNumber); 
		return QuestionTwo;
		}
		
		if(questionName == "QuestionThree"){
        Object[] QuestionThree = new Object[3];
        QuestionThree[0] = new double[] {-0.3071, 0.0575, 0.2964, -0.0185};
        QuestionThree[1] = new double[] {-0.2606, 0.1087, 0.1282, 0.04};
        QuestionThree[2] = new double[] {0, 0, 0, 0};
        //generateSegmentArray(QuestionThree,categoryNumber);
        return QuestionThree;
		}
        
		if(questionName == "QuestionFour"){
        Object[] QuestionFour = new Object[3];
        QuestionFour[0] = new double[] {-0.0646, 0.1162, 0.1938, -0.2364};
        QuestionFour[1] = new double[] {-0.0024, 0.0239, 0.1494, -0.1645};
        QuestionFour[2] = new double[] {0, 0, 0, 0};
        return QuestionFour;
       	}
		
		if(questionName == "QuestionFive"){
        Object[] QuestionFive = new Object[3];
        QuestionFive[0] = new double[] {-0.1381, 0.2427, 0, -0.1048};
        QuestionFive[1] = new double[] {-0.0247, 0.0782, 0.0256, -0.079};
        QuestionFive[2] = new double[] {0, 0, 0, 0};
        //generateSegmentArray(QuestionFive,categoryNumber);
        return QuestionFive;
		}
        
		if(questionName == "QuestionSix"){
        Object[] QuestionSix = new Object[3];
        QuestionSix[0] = new double[] {-0.0041, 0.2289, -0.082, -0.1534};
        QuestionSix[1] = new double[] {0.0321, 0.0666, 0.0105, -0.1124};
        QuestionSix[2] = new double[] {0, 0, 0, 0};
        //generateSegmentArray(QuestionSix,categoryNumber);
        return QuestionSix;
		}
        
		if(questionName == "QuestionSeven"){
        Object[] QuestionSeven = new Object[3];
        QuestionSeven[0] = new double[] {0.0657, 0.1792, -0.1542, -0.1068};
        QuestionSeven[1] = new double[] {0.0724, 0.0679, -0.0796, -0.0702};
        QuestionSeven[2] = new double[] {0, 0, 0, 0};
       // generateSegmentArray(QuestionSeven,categoryNumber);
	    return QuestionSeven;
		}
		return null;
		
        
       		
	}
	
	void generateSegmentArray(Object[] questionObject,int categoryNumber) {
		int count = 0;
		
		for (Object row: questionObject)
		{
			
			if(count == categoryNumber){
				double[] theRow = (double[])row;
	            for(int i= 0; i < theRow.length; i++){
	                System.out.print(theRow[i]);
	                System.out.print(" ");
	            
if(countadd == 0 || countadd == 4 || countadd == 8 || countadd == 12 || countadd == 16 || countadd == 20 || countadd == 24){
	one += theRow[i];
}

if(countadd == 1 || countadd == 5 || countadd == 9 || countadd == 13 || countadd == 17 || countadd == 21 || countadd == 25){
	two += theRow[i];
}

if(countadd == 2 || countadd == 6 || countadd == 10 || countadd == 14 || countadd == 18 || countadd == 22 || countadd == 26){
	three += theRow[i];
}

if(countadd == 3 || countadd == 7 || countadd == 11 || countadd == 15 || countadd == 19 || countadd == 23 || countadd == 27){
	four += theRow[i];
}
                
	                
	                countadd++;
	            }
	            System.out.print("\n");
			
			}
			count++;
		}		 
		
	}
	
	private String getSegmentName(int index)
	{
		String[] name = new String[] {"GOTSTGSEG","GOTSKPSEG","GOTSTUSEG","GOTINDSEG"}; 
		
		return name[index];
	}

	

	public String getMax() {
		one += 0.2885;
		two += -0.212;
		three += 0.131;
		four += 0.8001;
		double[] total = new double[] {one,two,three,four};
		double Max = 0;
		int segment = 0;
		for(int s = 0; s <= total.length-1; s++)
		{
			if(s != 3){
			if(s == 0)
			{
				if(total[s] > total[s+1]){
					Max = total[s];
					segment = s;
					
				}else{
					Max = total[s+1];
					segment = s+1;
					
				}
			}else{
				
				if(total[s+1] > Max  ){
					Max = total[s+1];
					segment = s+1;
					
				}
			}
			}
			
			
		}
		//System.out.println("====TOTAL====");
		//System.out.println(one+" "+two+" "+three+" "+four+"\n");		
		//System.out.println(getSegmentName(segment)+"=>"+Max);
		return getSegmentName(segment);
	}

		
	
	
}



