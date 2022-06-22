# BigQuery Column Level Encryption Example

Example to show how to encrypt a piece of data in Java and get the ciphertext that can be decrypted in BigQuery using BigQuery AEAD encrypt/decrypt functions.

To create data encryption keys:

```shell
# create plaintext keyset
tinkey create-keyset --key-template AES256_GCM --out-format json \
    --out "plaintext-dek.json"

# create encrypted keyset
gcloud kms keyrings create demo-keyring-us --location us --project <gcp-project-id>
gcloud kms keys create demo-key --keyring demo-keyring-us --purpose encryption --location us --project <gcp-project-id>
tinkey create-keyset --key-template AES256_GCM  \
    --master-key-uri "gcp-kms://projects/<gcp-project-name>/locations/us/keyRings/demo-keyring-us/cryptoKeys/demo-key" \
    --out "encrypted-dek.json"

```
