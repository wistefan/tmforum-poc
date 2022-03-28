## CPU Product Spec

```json
{
  "id": "urn:ngsi-ld:cpu:nvidia-xeon",
  "name": "Xeon CPU",
  "description": "CPU Instance",
  "relatedParty": [
    {
      "id": "urn:ngsi-ld:organization:aws",
      "role": "Operator"
    }
  ],
  "productSpecCharacteristic": [
    {
      "name": "hasJurisdiction",
      "valueType": "string",
      "productSpecCharacteristicValue": [
        {
          "valueType": "string",
          "value": "Germany"
        }
      ]
    },
    // more flat attributes
    {
      "name": "HardwareSpec",
      "valueType": "VerifiableCredential",
      "productSpecCharacteristicValue": [
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
                "HardwareSpec"
              ],
              "issuer": "urn:nvidia",
              "issuanceDate": "2010-01-01T19:23:24Z",
              "credentialSubject": {
                "id": "urn:ngsi-ld:cpu:nvidia-xeon",
                "resource": {
                  "hasName": "Xeon CPU",
                  "hasDescription": "CPU Instance",
                  "hasJurisdiction": "Germany",
                  "isOperatedBy": "urn:ngsi-ld:organization:aws",
                  "location": {
                    "latitude": 50.1109,
                    "longitude": 8.6821
                  },
                  "isComposedBy": [
                    {
                      "hasProductID": "6CX68AV",
                      "hasTitle": "Xeon Platinum 8280",
                      "hasManufacturer": "NVIDIA",
                      "hasNumberOfCores": 4,
                      "hasNumberOfThreads": 12,
                      "hasFrequency": {
                        "hasUnit": "GHz",
                        "hasValue": 3.0
                      },
                      "hasBoostFrequency": {
                        "hasUnit": "GHz",
                        "hasValue": 3.0
                      },
                      "hasCache": {
                        "hasUnit": "MB",
                        "hasValue": 38.0
                      },
                      "hasAllowedSocket": "hasAllowedSocket"
                    }
                  ]
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
      ]
    }
  ]
}
```

## Network device product spec

```json
{
  "id": "urn:ngsi-ld:switch:cisco",
  "name": "Network in Frankfurt",
  "description": "Networking device in the frankfurt datacenter",
  "relatedParty": [
    {
      "id": "urn:ngsi-ld:organization:aws",
      "role": "Operator"
    }
  ],
  "productSpecCharacteristic": [
    {
      "name": "hasJurisdiction",
      "valueType": "string",
      "productSpecCharacteristicValue": [
        {
          "valueType": "string",
          "value": "Germany"
        }
      ]
    },
    // more flat attributes
    {
      "name": "HardwareSpec",
      "valueType": "VerfiableCredential",
      "productSpecCharacteristicValue": [
        {
          "valueType": "VerfiableCredential",
          "value": {
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
                  "HardwareSpec"
                ],
                "issuer": "urn:cisco",
                "issuanceDate": "2010-01-01T19:23:24Z",
                "credentialSubject": {
                  "id": "urn:ngsi-ld:switch:cisco",
                  "resource": {
                    "hasName": "Network in Frankfurt",
                    "hasDescription": "Networking device in the frankfurt datacenter",
                    "location": {
                      "latitude": 50.1109,
                      "longitude": 8.6821
                    },
                    "hasJurisdication": "Germany",
                    "isOperatedBy": "urn:ngsi-ld:organization:aws",
                    "hasManagementPort": true,
                    "hasConsolePort": true,
                    "hasPortCapacity_A": 10.0,
                    "hasPortCapacity_A_Count": 10,
                    "hasRedundantPowerSupply": false,
                    "hasRAMSize": {
                      "hasUnit": "Gb",
                      "hasValue": 2
                    },
                    "hasCPUCount": 2,
                    "hasType": "switch",
                    "hasSupportedProtocols": "IP, RP",
                    "hasNetworkAdress": "192.168.24.2/32"
                  }
                }
              }
            ]
          }
        }
      ]
    }
  ]
}
```
## Product offering for the service
```json
{
  "id": "urn:ngsi-ld:offering:iaas",
  "description": "Infrastructure resources as a service",
  "isBundle": false,
  "name": "IaaS",
  "relatedParty": [
    {
      "id": "urn:ngsi-ld:organization:aws",
      "role": "Definer"
    }
  ],
  "productSpecification": [
    {
      "id": "urn:ngsi-ld:cpu:nvidia-xeon"
    },
    {
      "id": "urn:ngsi-ld:switch:cisco"
    }
  ],
  "prodSpecCharValueUse": [
    {
      "name": "ServiceOffering",
      "valueType": "VerifiableCredential",
      "productSpecCharacteristicValue": [
        {
          "valueType": "VerifiableCredential",
          "value": {
            "@context": [
              "https://www.w3.org/2018/credentials/v1",
              "https://www.w3.org/2018/credentials/examples/v1"
            ],
            "id": "http://example.edu/credentials/1872",
            "type": [
              "VerifiableCredential",
              "ServiceOffering"
            ],
            "issuer": "urn:ngsi-ld:organization:aws",
            "issuanceDate": "2010-01-01T19:23:24Z",
            "credentialSubject": {
              "id": "urn:ngsi-ld:offering:iaas",
              "offering": {
                "hasServiceTitle": "IaaS",
                "description": "Infrastructure resources as a service",
                "keyword": "VM",
                "isDefinedBy": "urn:ngsi-ld:organization:aws",
                "hasProvisionType": "Hybrid",
                "isComposedBy": [
                  {
                    "hasName": "VM in Frankfurt",
                    "hasDescription": "VM resource located in the eu",
                    "location": {
                      "latitude": 50.1109,
                      "longitude": 8.6821
                    },
                    "hasJurisdication": "Germany",
                    "isOperatedBy": "urn:ngsi-ld:organization:aws",
                    "isComposedBy": [
                      "urn:ngsi-ld:cpu:nvidia-xeon",
                      "urn:ngsi-ld:switch:cisco"
                    ]
                  }
                ]
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
      ]
    }
  ]
}

```