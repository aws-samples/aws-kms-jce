package software.amazon.awssdk.services.kms.jce.provider.ec;

import software.amazon.awssdk.services.kms.jce.provider.KmsPublicKey;

import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;

public class KmsECPublicKey implements KmsPublicKey, ECPublicKey {

    private static final long serialVersionUID = 1L;
	private final String id;
    private final ECPublicKey publicKey;

    public KmsECPublicKey(String id, ECPublicKey publicKey) {
    	this.id = id;
    	this.publicKey = publicKey;
    }
    
    @Override
    public ECPoint getW() {
        return publicKey.getW();
    }

    @Override
    public String getAlgorithm() {
        return publicKey.getAlgorithm();
    }

    @Override
    public String getFormat() {
        return publicKey.getFormat();
    }

    @Override
    public byte[] getEncoded() {
        return publicKey.getEncoded();
    }

    @Override
    public ECParameterSpec getParams() {
        return publicKey.getParams();
    }

    @Override
	public String getId() {
		return id;
	}

    public PublicKey getPublicKey() {
    	return publicKey;
    }
}
