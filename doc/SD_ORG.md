
```json
 {
  "@context": [
    "https://www.w3.org/2018/credentials/v1",
    "https://www.w3.org/2018/credentials/examples/v1"
  ],
  "id": "urn:uuid:3978344f-8596-4c3a-a978-8fcaba3903c5",
  "type": [
    "VerifiablePresentation",
    "GAIA-X-SelfDescription"
  ],
  "verifiableCredential": [
    {
      "@context": [
        "https://www.w3.org/2018/credentials/v1",
        "https://www.w3.org/2018/credentials/examples/v1"
      ],
      "id": "http://example.edu/credentials/1872",
      "type": [
        "VerifiableCredential",
        "CompanyRegistration"
      ],
      "issuer": "urn:gewerbeamt-münchen",
      "issuanceDate": "2010-01-01T19:23:24Z",
      "credentialSubject": {
        "id": "urn:ngsi-ld:organization:aws",
        "provider": {
          "hasLegallyBindingName": "AWS Germany",
          "hasLegallyBindingAddress": {
            "street-address": "Oskar-von-Miller-Ring 20",
            "locality": "80333 München",
            "country-name": "Germany"
          },
          "hasLegalForm": "GmbH",
          "hasJurisdiction": "Germany",
          "hasVATnumber": "DE 129515865",
          "hasLegalRegistrationNumber": "HRB 12",
          "hasWebAddress": "web.de"
        }
      },
      "proof": {
        "type": "RsaSignature2018",
        "created": "2017-06-18T21:19:10Z",
        "proofPurpose": "assertionMethod",
        "verificationMethod": "https://example.edu/issuers/565049#key-1",
        "jws": "eyJhbGciO....QyHUdBBPM"
      }
    }
  ],
  "proof": {
    "type": "RsaSignature2018",
    "created": "2018-09-14T21:19:10Z",
    "proofPurpose": "authentication",
    "verificationMethod": "did:example:ebfeb1f712ebc6f1c276e12ec21#keys-1",
    "challenge": "1f44d55f-f161-4938-a659-f8026467f126",
    "domain": "4jt78h47fh47",
    "jws": "eyJhbGciOiJSUzI1NiIsImI2NCI6ZmFsc2UsImNyaXQiOlsiYjY0Il19..kTCYt5"
  }
}
```
