```json
{
  "name": "AWS Germany",
  "nameType": "GmbH",
  "organizationType": "GmbH",
  "tradingName": "AWS Germany",
  "contactMedium": [
    {
      "mediumType": "Address",
      "preferred": true,
      "characteristic": {
        "city": "München",
        "contactType": "postal address",
        "country": "Germany",
        "postCode": "80333",
        "street1": "Oskar-von-Miller-Ring 20"
      }
    }
  ],
  "partyCharacteristic": [
    {
      "name": "CompanyRegistration",
      "valueType": "VerifiableCredential",
      "value": {
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
    }
  ],
  "status": "initialized"
}
```