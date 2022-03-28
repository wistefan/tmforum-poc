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
```