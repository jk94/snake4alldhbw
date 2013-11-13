package Connection;

import java.math.BigInteger;

public class SharedValue {
	BigInteger encryptKey;
	public BigInteger getEncryptionKey() {
		BigInteger decryptionKey_copy = this.encryptKey;
		
		return decryptionKey_copy;
	}
	public void setEncryptionKey(BigInteger decryptionKey) {
		this.encryptKey = decryptionKey;
	}
	public SharedValue(){
		
	}
}
