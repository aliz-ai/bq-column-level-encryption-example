package aliz.examples;

import com.google.crypto.tink.Aead;
import com.google.crypto.tink.CleartextKeysetHandle;
import com.google.crypto.tink.JsonKeysetReader;
import com.google.crypto.tink.KeysetHandle;
import com.google.crypto.tink.aead.AeadConfig;
import com.google.crypto.tink.aead.KmsAeadKeyManager;
import com.google.crypto.tink.integration.gcpkms.GcpKmsClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Base64;
import java.util.Optional;

public class Encryption {
    private static byte[] PLAIN_TEXT = "hello world".getBytes(StandardCharsets.UTF_8);
    private static byte[] ASSOCIATED_DATA = "tink".getBytes(StandardCharsets.UTF_8);

    public static void encryptWithPlaintextKeyset() throws IOException, GeneralSecurityException {
        KeysetHandle keysetHandle =
                CleartextKeysetHandle.read(JsonKeysetReader.withPath(Paths.get("plaintext-dek.json")));
        AeadConfig.register();
        Aead aead = keysetHandle.getPrimitive(Aead.class);
        byte[] cipherText = aead.encrypt(PLAIN_TEXT, ASSOCIATED_DATA);
        String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);
        System.out.printf("Example: encrypting with plain keyset --> %s%n", cipherTextBase64);
    }

    public static void encryptWithEncryptedKeyset() throws IOException, GeneralSecurityException {
        AeadConfig.register();
        String kekUri = "gcp-kms://projects/<gcp-project-id>/locations/us/keyRings/demo-keyring-us/cryptoKeys/demo-key";
        GcpKmsClient.register(Optional.of(kekUri), Optional.empty());
        KeysetHandle kekHandle = KeysetHandle.generateNew(KmsAeadKeyManager.createKeyTemplate(kekUri));
        Aead keyAead = kekHandle.getPrimitive(Aead.class);
        KeysetHandle keysetHandle = KeysetHandle.read(JsonKeysetReader.withPath(Paths.get("encrypted-dek.json")), keyAead);
        Aead aead = keysetHandle.getPrimitive(Aead.class);
        byte[] cipherText = aead.encrypt(PLAIN_TEXT, ASSOCIATED_DATA);
        String cipherTextBase64 = Base64.getEncoder().encodeToString(cipherText);
        System.out.printf("Example: encrypting with encrypted keyset --> %s%n", cipherTextBase64);
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.printf("Input plaintext: %s%n", new String(PLAIN_TEXT, StandardCharsets.UTF_8));
        encryptWithPlaintextKeyset();
        encryptWithEncryptedKeyset();
    }
}

