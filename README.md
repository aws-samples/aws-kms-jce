## AWS KMS JCE

The AWS KMS JCE Provider software library for Java is a vendor implementation for the Sun Java JCE (Java Cryptography Extension) provider framework with a focus on using asymmetric keys to sign and verify. 
This includes implementations for interfaces and engine classes in the JCA (Java Cryptography Architecture) standard.

This repository is a multi-module project composed of the following modules:
* **provider**: contains the AWS KMS JCE Provider implementation
* **util**: contains utility classes for generating CSR and Self-Signed Certificate

### How to use

To use the AWS KMS JCE Provider, you first need to [create an asymmetric CMK in the AWS KMS](https://docs.aws.amazon.com/kms/latest/developerguide/create-keys.html) for signature and verification. 
An asymmetric CMK represents a mathematically related public key and private key pair. It is possible to give the public key to anyone, even if they are not trusted, but the private key must be kept secret.

AWS KMS supports two types of asymmetric CMKs for signing and verification:
* **RSA CMKs**: a CMK with an RSA key pair. KMS supports multiple key sizes for different security requirements.
* **Elliptical curve CMKs (ECC)**: a CMK with a pair of elliptical curve keys. KMS supports several commonly used curves.

Both types are supported on the AWS KMS JCE Provider, as well as all of its size [variations and all of its signature algorithms](https://docs.aws.amazon.com/kms/latest/developerguide/symm-asymm-choose.html).

#### Initializing the Provider

To use the AWS KMS JCE Provider it is necessary to first create the provider. 
The provider needs the KMS client to be informed in its creation. It is worth mentioning that the creation of the client can and should be customized for each case.

```
KmsClient kmsClient = KmsClient.builder().build();
KmsProvider kmsProvider = new KmsProvider(kmsClient);
```

Once the provider is created, it is possible to register it.

```
Security.addProvider(kmsProvider);
```

#### Getting key references

You can obtain key references in two ways:
* Via KeyFactory: **KmsRSAKeyFactory** and **KmsECKeyFactory**
* Via KeyStore: **KmsKeyStore**

The difference between the two mentioned ways is that to use the KeyFactory it is necessary to inform the key Id and for the use of the KeyStore the key alias is used. 
It is worth mentioning that the KeyStore uses the KeyFactory, that is, from the informed alias the key Id is recovered and then the KeyFactory is used. Always prioritize the use of KeyFactory to obtain better performance.

To use the KeyFactory (KmsRSAKeyFactory or KmsECKeyFactory) just invoke its static methods.

```
KmsECKeyFactory.getKeyPair(...);
KmsECKeyFactory.getPrivateKey(...);
KmsECKeyFactory.getPublicKey(...);
...
KmsRSAKeyFactory.getKeyPair(...);
KmsRSAKeyFactory.getPrivateKey(...);
KmsRSAKeyFactory.getPublicKey(...);
```

To use the KeyStore, you must obtain the KeyStore via the JCE standard and initialize it.

```
KeyStore keyStore = KeyStore.getInstance("KMS");
keyStore.load(null, null);
...
keyStore.aliases();
keyStore.containsAlias(...);
keyStore.size();
keyStore.getKey(...);
```

#### Signing and verifying


To facilitate the use of the library, the enum **KmsSigningAlgorithm** was created, which has the mapping to use the desired algorithm. 
Where the following mapping is present:

KMS signature algorithm | Java signature algorithm |
:-: | :-: |
RSASSA_PSS_SHA_256 | RSASSA-PSS/SHA256 |
RSASSA_PSS_SHA_384 | RSASSA-PSS/SHA384 |
RSASSA_PSS_SHA_512 | RSASSA-PSS/SHA512 |
RSASSA_PKCS1_V1_5_SHA_256 | SHA256withRSA |
RSASSA_PKCS1_V1_5_SHA_384 | SHA384withRSA |
RSASSA_PKCS1_V1_5_SHA_512 |	SHA512withRSA |
ECDSA_SHA_256 |	SHA256withECDSA |
ECDSA_SHA_384 |	SHA384withECDSA |
ECDSA_SHA_512 |	SHA512withECDSA |

To sign and / or verify it is necessary to obtain Signature via JCE:

```
KmsSigningAlgorithm kmsSigningAlgorithm = KmsSigningAlgorithm.<X>;

Signature kmsSignature = Signature.getInstance(kmsSigningAlgorithm.getAlgorithm());

// Signing...
kmsSignature.initSign(privateKey);
kmsSignature.update(message.getBytes());
byte[] signatureBytes = kmsSignature.sign();

// verifying...
kmsSignature.initVerify(publicKey);
kmsSignature.update(message.getBytes());
boolean valid = kmsSignature.verify(signatureBytes);
```

#### Generating the Certificate Signing Request (CSR) and Self-Signed Certificate

Before generating the CSR, it is necessary to first define the information that will be present in the CSR. For this, **CsrInfo** is used.

```
CsrInfo csrInfo = CsrInfo.builder()
        .cn("...") //Common Name
        .ou("...") //Department Name / Organizational Unit
        .o("...") //Business name / Organization
        .l("...") //Town / City
        .st("...") //Province, Region, County or State
        .c("...") //Country
        .mail("...") //Email address
        .build();
```

After creating CsrInfo, it is possible to generate the CSR:

```
String csr = CsrGenerator.generate(keyPair, csrInfo, kmsSigningAlgorithm);
```

With the generated CSR, it is possible to generate the Self-Signed Certificate (if necessary):

```
int validity = 365; //In days
String crt = SelfSignedCrtGenerator.generate(keyPair, csr, kmsSigningAlgorithm, validity);
```

A complete example of generating CSR and Self-Signed Certificate can be seen below:

```
public class Example {

    public static void main(String[] args) {
        KmsClient kmsClient = KmsClient.builder().build();
        Security.addProvider(new KmsProvider(kmsClient));

        KeyPair keyPair = KmsRSAKeyFactory.getKeyPair(kmsClient, KeyIds.SIGN_RSA);
        KmsSigningAlgorithm kmsSigningAlgorithm = KmsSigningAlgorithm.RSASSA_PKCS1_V1_5_SHA_256;

        CsrInfo csrInfo = CsrInfo.builder()
                .cn("kms.aws.amazon.com")
                .ou("AWS")
                .o("Amazon")
                .l("Sao Paulo")
                .st("Sao Paulo")
                .c("BR")
                .mail("kms@amazon.com")
                .build();

        System.out.println("CSR Info: " + csrInfo.toString());
        System.out.println();

        String csr = CsrGenerator.generate(keyPair, csrInfo, kmsSigningAlgorithm);
        System.out.println("CSR:");
        System.out.println(csr);

        String crt = SelfSignedCrtGenerator.generate(keyPair, csr, kmsSigningAlgorithm, 365);
        System.out.println("CRT:");
        System.out.println(crt);
    }

}
```

## Security

See [CONTRIBUTING](CONTRIBUTING.md#security-issue-notifications) for more information.

## License

This library is licensed under the MIT-0 License. See the LICENSE file.