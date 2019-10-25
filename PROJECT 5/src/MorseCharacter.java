/*******************
 * Christine Do
 * Spring 2019
 * CMSC 256 - 003
 * Description: this class is used to instantiate MorseCharacter objects
 */
@SuppressWarnings("rawtypes")
public class MorseCharacter implements Comparable{ //<K>

	//instance variable character
	private String character;
	
	//default constructor
	public MorseCharacter() {
		character = "";
	}
	
	//parameterized constructor sets instance variable to the String that is passed to the method
	public MorseCharacter(String c) {
		character = c;
	}
	
	//returns character variable
	public String getCode() {
		return character;
	}
	
	//sets instance variable to String that is passed to the method
	public void setCode(String morseChar) {
		this.character = morseChar;
	}
	
	//returns String representation
	public String toString() {
		return character;
	}
	
	/*
	 * compares two MorseCharacter objects based on morse order
	 */
	@Override
	public int compareTo(Object o) {
		MorseCharacter mChar = (MorseCharacter) o;
		
		if(this.getCode().compareTo(mChar.getCode()) > 0) {
			if(this.getCode().length() > mChar.getCode().length()){
				return 1;
			}
			return -1;
			}
		else if(this.getCode().compareTo(mChar.getCode()) < 0) {
			if(this.getCode().length() < mChar.getCode().length()){
			return -1;
			}
			return 1;
			}
		else {return 0;}
		
		/*
		if(mChar.getCode().contains(this.getCode())) {
			if(mChar.getCode().length() < this.getCode().length()) {return -1;}
			if(mChar.getCode().length() > this.getCode().length()) {return 1;}
			else {return 0;}
		}
		else if(this.getCode().contains(mChar.getCode())) {
			if(this.getCode().length() < mChar.getCode().length()) {return -1;}
			else if(this.getCode().length() > mChar.getCode().length()) {return 1;}
			else {return 0;}
	
		}
		else {
		return mChar.getCode().compareTo(this.getCode());
		}
		*/
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = prime * result + ((character == null) ? 0 : character.hashCode());
		return result;
	}
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MorseCharacter other = (MorseCharacter) obj;
		if (character == null) {
			if (other.character != null)
				return false;
		} else if (!character.equals(other.character))
			return false;
		return true;
	}
}
