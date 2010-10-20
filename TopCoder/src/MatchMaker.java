import java.util.Arrays;


public class MatchMaker {
	
	public String[] getBestMatches(String[] members, String currentUser, int sf) {
		
		int numSel = 0;
		String[] memberNames = new String[members.length];
		int[] memberScores = new int[members.length];
		
		String[] pCurrentUser = null;
		String[][] pMembers = new String[members.length][];
		for (int i = 0; i < members.length; i++) {
			pMembers[i] = members[i].split(" ");
			if(pMembers[i][0].equals(currentUser)) 
				pCurrentUser = pMembers[i];
		}
		
		//no user found
		if(pCurrentUser == null) return null;
		
		for (int i = 0; i < pMembers.length; i++) {
			String[] pMember = pMembers[i];
			
			//skip current user
			if(pMember[0].equals(pCurrentUser[0])) continue;
			
			//skip sex incompatible users
			if(!pMember[1].equals(pCurrentUser[2])) continue;
				
			//count score (common answers)
			int score = 0;
			
			for (int j = 3; j < pMember.length; j++) {
				if(pMember[j].equals(pCurrentUser[j])) score++;
			}
			
			//test if score is enough
			if(score < sf) continue;
			
			//score is good, insert in proper place
			int j = numSel;
			while(j > 0 && score > memberScores[j - 1]) {
				memberScores[j] = memberScores[j - 1];
				memberNames[j] = memberNames[j - 1];
				j--;
			}
			
			memberNames[j] = pMember[0];
			memberScores[j] = score;
			
			numSel++;
		}
	
		String[] res = new String[numSel];
		System.arraycopy(memberNames, 0, res, 0, numSel);
		return res;
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.asList(new MatchMaker().getBestMatches(new String[]{
				"BOB M M A", 
				"FRED M F A", 
				"JIM F M A", 
				"DAISY F F A"}, "BOB", 1)));
	}
}