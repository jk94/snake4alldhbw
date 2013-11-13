package Main;

import java.math.BigInteger;

public class SharedValue {
	BigInteger decryptionKey;
	public BigInteger getDecryptionKey() {
		BigInteger decryptionKey_copy = this.decryptionKey;
		
		return decryptionKey_copy;
	}
	public void setDecryptionKey(BigInteger decryptionKey) {
		this.decryptionKey = decryptionKey;
	}
	public SharedValue(){
		
	}
}
