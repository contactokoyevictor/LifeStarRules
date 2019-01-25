/**
 *
 * @author Pkb
 */
package com.novartis.messaging.preps;


public class GotBPMessageTips {


	 public Object[] getCoefficientValues(String tipname)
		{
			if(tipname == "tipOne"){
			Object[] One = new Object[4];
	        One[0] = new String[] {"GetOnTrack BP Tip: Ever scramble for a refill? Mark your calendar 1 week before your prescription is scheduled to run out to skip the stress, not your meds."};
	        One[1] = new String[] {"GetOnTrack BP Tip: Socks. Your meds. Keep them together so getting dressed reminds you to take your meds. (Of course, store properly and keep away from kids.)"};
	        One[2] = new String[] {"GetOnTrackBP Tip: A great parking spot is nice. But circling a lot doesn�t help a high BP. Park in the back for a workout. OK exercise with the Dr first."};
	        One[3] = new String[] {"GetOnTrack BP Tip: Up to 75% of the salt we eat is from processed foods like cheese & canned veggies. Adding even more table salt can add up for your BP."};
	        return One;

			}
			if(tipname == "tipTwo"){
	        Object[] Two = new Object[4];
	        Two[0] = new String[] {"GetOnTrack BP Tip: Even though you can�t see high BP, it�s no laughing matter. It�s not a good idea to skip your meds, even if you don�t have symptoms."};
	        Two[1] = new String[] {"GetOnTrack BP Tip: Keep an eye on your BP #s! Uncontrolled high BP can cause thickened, narrowed, or torn blood vessels in the eyes, leading to vision loss."};
	        Two[2] = new String[] {"GetOnTrack BP Tip: Reach for your meds when you set down your toothbrush to make taking them feel like second nature, too."};
	        Two[3] = new String[] {"GetOnTrack BP Tip: Watch your weight. A healthy weight can lower your risk of heart attack and stroke, and help you manage your high BP."};

			return Two;
			}
			return null;

		}

	public String GetMessageTip(Object[] questionObject,int randNumber) {
			int count = 0;
			String message = "";
			for (Object row: questionObject)
			{

				if(count == randNumber){
					String[] theRow = (String[])row;
		            for(int i= 0; i < theRow.length; i++){
		                //System.out.print(theRow[i]);
		               // System.out.print(" ");
		                message = theRow[i];

		            }
		            //System.out.print("\n");

				}
				count++;
			}
			return message;

}



}