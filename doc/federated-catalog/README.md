# Analysis of the GAIA-X Federated Catalog API

This document contains a short analysis of the federated catalog api(GAIA-X) in the context of compatibility with the TMForum-APIs.

> **TLDR:** Both APIs follow a normal REST-approach, with TMForum being much more detailed on the concrete resources. The Federated Catalog doesn't care about concrete data, but only about their representation in form of self-descriptions and the life-cycle of a self-description. While most parts are compatible, 4 challenges where identified:
> 
> * [Life-Cycle Mapping](#lifecycle-mapping): TMForum handles the lifecycle of its resources, Federated Catalog lifecycle of self-descriptions - easy to solve
> * [Graph-Structure](#graph-structure): Federated Catalog enforces the usage of graph-structure and graph-query languages. TMForum does not provide rich querying-functionality, but if NGSI-LD will be used as a connection, the graph-queries need to be translated into NGSI-LD queries. TMForum implementation needs to create graph-structures(f.e. NGSI-Relationships)
> * [Unclarity on Participant/User-representation as self-description](#unclarity-on-participantuser-representation-as-self-description): Representation of participants/users as self-descriptions is inconsistent between API-schema and surrounding documentation. Needs to be clarified.
> * [User Handling](#user-handling): Federated Catalog mixes IAM and Data. This has to be investigated and clarified deeper for proper implementation, but looks solveable. 

# Links

* Gaia-X Federated Catalog:

    * Spec: [Gitlab - Federation-Service-Specification](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md)

    * API: [Gitlab - Federation-Service-Specification - API](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#appendix-b-rest-api)
    * Analyzed version of the api: [Commit 70d7edf7177147912ff33ff8c95fdd9cce805430](catalog_70d7edf7177147912ff33ff8c95fdd9cce805430.yaml)

* TMForum APIs:

    * [Open API Table](https://projects.tmforum.org/wiki/display/API/Open+API+Table)

# Federated Catalog analysis

## discovery - ```/```

no direct mapping, not important. Similar to /.well-known in oidc

## self-descriptions - ```/self-descriptions```

Simple rest endpoint to CRUD self-descriptions. Hash of self-description is used as the id. 
Only difference to plain REST is due to the immutability of self-descriptions. Since only the life-cycle phase is allowed to change([There are
four possible states for the Self-Description life cycle: “active” (the
default), “revoked”, “deprecate” and “end-of-life”.](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#product-perspective)), PUT can only be used for this transition. 


TMForum is more detailed and a advanced, but uses the same concept. Due to the immutability, the implementation that generates self-descriptions out of TMForum-resources needs to take care of life-cycle transitions and creation of new self-descriptions in case of change. 

### Example:

* **TMForum** - ```POST /entitySpecification```
   * create entity-spec in db
   * generate self-description for the entity-spec
   * set life-cycle phase to "active"
* **Federated-Catalog** - ```GET /self-descriptions/{self_description_hash}```
   * return self-description of entity-spec
   * provide life-cycle phase "active"
* **TMForum** - ```PATCH /entitySpecification/{id}```
   * change entity-spec in db
   * generate new self-description for the spec
   * transition life-cycle phase of previous self-description to ```deprecate```
* **Federated-Catalog** - ```GET /self-descriptions/{OLD_HASH}```
   * return self-description of entity-spec
   * provide life-cycle phase "deprecated" 
* **Federated-Catalog** - ```GET /self-descriptions/{NEW_HASH}```
   * return self-description of entity-spec
   * provide life-cycle phase "active" 

## query - ```/queries```

Supports up to 2? query-languages:
* [openCypher](https://opencypher.org/): 
   * graph-database query-language - implementation of [GQL](https://www.iso.org/standard/76120.html)
* [sparql](https://www.w3.org/TR/rdf-sparql-query/)   
   * (graph) query language for rdf

See [Challenges-Section](#graph-structure) for more details.

## users - ```/users```

Endpoint for user-management if catalog is not connected to an IAM.

> :bulb: Recommendation(from me): use an IAM. 

Since users can be added to participants(and therefor are somewhat similar to ```Individuals``` in TMForum), a synchronization between the systems might be required. See [Challenges](#user-handling) for details. 

## participants - ```/participants```

Participants are represented by self-descriptions. Its not totally clear(to me) if they reference a Self-Description from the [self-descriptions endpoint](#self-descriptions---self-descriptions) or if the self-description is created in-line. If ```id```, ```name``` and ```public-key``` are not part of the self-description it would be inconsistent. Similar to that, there is no "back-link" from a ```participant``` to the ```user```. Therefor the ```user``` is also not part of the (verifiable) self-description, wich seems to be inconsistent from the trust-perspective.

[Participants](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#user-classes-and-characteristics) can provide or consume services and data:
> A participant can be a provider of services and data or a consumer,
which uses the provided services/data. Of course, a participant could
have both roles at the same time. Each participant has to be registered
in Gaia-X and is issued a certificate which is needed for identification
within Gaia-X. The certification information for participants is managed
by the IAM services by WP1[3] and issued in the form of Verifiable
Credentials.

The participants api is similar to TMForum's [Party-API](https://tmf-open-api-table-documents.s3.eu-west-1.amazonaws.com/OpenApiTable/4.0.0/swagger/TMF632-Party-v4.0.0.swagger.json). The participant maps to the ```Organization``` in TMForum. Both APIs support REST-CRUD, with the same exception on updates due to the [Lifecycle](#lifecycle-mapping).

See [Challenges](#user-handling) for open questions in regards to this endpoint.

## schemas - ```/schemas```

Informational endpoint about the used schemas for self-descriptions. No mapping required due to TMForum having its own schema.

## roles - ```/roles```

Endpoint to get available roles. Similar to [Challenges - User handling](#user-handling) its mixing IAM and Data-Model. Roles (seem to be) hardcoded for the system(either imported from IAM or directly) and can be linked by the user.
The endpoint only allows retrieval of available roles.
The doc in the [spec mentions](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#Role%20Assignment):
>A role is stored as a Verifiable Credentials (VC) of a user. The VC is
signed by the participant to confirm the validity of the content. If an
additional role is assigned to a user, the participant is to create and
sign a new VC of the user with the newly added role.

This indicates that users will also be stored as some kind of self-description. If so, the TMForum implementation needs to generate self-descriptions for ```individuals```.

## verfication - ```/verification/self-descriptions```
 
Convinience endpoint for verifing the syntax of a self-description.

## session - ```/sessions```

Seems to be a fragment from IAM, no further documentation.

# Challenges

## Lifecycle mapping
 
Since self-descriptions are immutable, only their [lifecycle](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#L805)(attached as metadata) can be changed. The supported states are:

*  Active(default)
*  End-of-Life
*  Deprecated
*  Revoked

Only ```Active``` is a non-terminal state. The TMForum implementation(or any other connector) needs use this state in case of updating information. See [Self-Description example](#example). The TMForum lifecycle needs to be part of the data inside the self-description:
> Note that the state of the Self-Description is independent from the
state of the underlying entity. For example, a Service Offering can be
deprecated (e.g., replaced by a newer version), whereas the
Self-Description for the Service Offering (in that version) is still
active.

Thus, mapping will be easy(naming can provide confusion, but no technical issues). TMForum resources will have a 1-1...* relationship to self-descriptions, 1 self-description for each update.

## Graph structure 

The spec requires a graph-structure for the self-descirptions: [Product Perspective](https://gitlab.com/gaia-x/technical-committee/federation-services/federation-service-specifications/-/blob/master/docs/L05_FC_CCF/fc_ccf.md#product-perspective)
> The Self-Description Graph contains the information imported from the
Self-Descriptions that are known to the Catalogue and in an “active”
life cycle state. The Self-Description Graph allows for complex queries
across Self-Descriptions.

Since [NGSI-LD](https://docbox.etsi.org/isg/cim/open/Latest%20release%20NGSI-LD%20API%20for%20public%20comment.pdf) relationships can be seen as a graph-structure, it might be possible to keep the self-description graph inside NGSI-LD. 
To fully work with the federated-catalog, this requires:

* the TMForum-Application to porperly maintain the graph(relatively easy)
* the Federated-Catalog implementation to translate graph-queries into NGSI-LD queries(possible in theory, maybe challanging, needs to be investigated)

## Unclarity on Participant/User-representation as self-description

While the spec suggests that participants and users should be self-descriptions, the APIs schema does not reflect this properly. 
### User as defined by the API:
```json
{
    "id": "ExampleCompany-John-Doe",
    "participantId": "ExampleCompany",
    "username": "John Doe",
    "email": "string",
    "roleIds": [
        {
        "id": "Ro-MU-CA"
        }
    ]
}
```
No self-description at all, even though the description says: 
> A role is stored as a Verifiable Credentials (VC) of a user. 

### Participant as defined by the API:
```json
{
  "id": "string",
  "name": "string",
  "public-key": "string",
  "self-description": "string"
}
```
```id```, ```name``` and ```public-key``` are either duplicates from the self-description or break the chain of trust. Participant has no way to control or verfy its assigned users(can be solved f.e. with a VC on the user). 

## User handling

Since the Federated Catalog is mixing IAM with its datamodel, some kind of user-federation between the systems will be required.

There are (as of now) 3 potential sources of users:

### Federated Catalog without IAM

The users are created inside the catalog, through the [users-endpoint](#users---users). No special handling required for the Federated Catalog, but they need to be translated into ```individuals``` on TMForum. When connected to an ```organization```, this maybe needs to be reflected into the self-description of the ```Participant```. The schemas inside the API suggest that the users are not part of the self-description, but that seems weird from a consistency and trust point of view.

### Federated Catalog with IAM

Totally unclear how connection between users in IAM and ```participant``` should be resolved. Possible solution: sync users from IAM to the catalog. Open: how to connect the user to the participant there?

### Individuals created via TMForum

Can be directly mapped into ```User``` from Federated Catalog([User-API](#users---users)). Data-Model mapping will be challenging, since the TMForum user can be connected to 0...* parties(especially organisations), while Federated Catalog requires a single participant for each user.

>:warning: Its important to clarify the usage of self-descriptions for users and participants. Current API looks very inconsistent in that aspect.
