# One ERP Kernel
## System Overview & Architectural Introduction

### 1. What This System Is
This is **not**:
* A hospital management system.
* A billing application.
* A pharmacy system.
* [cite_start]A set of CRUD microservices. [cite: 4, 5, 6, 7, 8]

It is a **full ERP platform kernel** designed to:
* [cite_start]Support healthcare modules (HIS, EMR, Lab, Pharmacy, Finance, HR, Quality, etc.). [cite: 10, 11, 12]
* [cite_start]Support future industry verticals. [cite: 13]
* [cite_start]Support third-party marketplace extensions. [cite: 14]
* [cite_start]Support custom development inside the platform. [cite: 15]
* [cite_start]Operate in highly regulated, multi-tenant, international environments. [cite: 16]

**Architectural Comparison:**
[cite_start]It is closer to SAP NetWeaver, Oracle ERP, Salesforce + AppExchange, or the ServiceNow platform than a typical Spring Boot + Angular application. [cite: 17, 18, 19, 20, 21, 22]

---

### 2. Core Philosophy
[cite_start]The system is built on five foundational principles: [cite: 24]

#### 2.1 Clean Core
* [cite_start]The kernel and standard modules must remain upgrade-safe. [cite: 26]
* [cite_start]No direct modification of core tables. [cite: 28]
* [cite_start]Extensions occur via controlled extension points and **Z namespaces**. [cite: 29, 30]
* [cite_start]Marketplace modules must not compromise platform integrity. [cite: 31]

#### 2.2 Platform Before Application
We are building a platform that includes:
* [cite_start]A metadata-driven system (DDIC). [cite: 34]
* [cite_start]A rules engine and workflow engine. [cite: 35, 36]
* [cite_start]A job scheduler and print architecture. [cite: 37, 38]
* [cite_start]A security framework and multi-tenant runtime. [cite: 39, 40]
* [cite_start]An IDE (**One Studio**). [cite: 41]
[cite_start]Applications are built on top of this kernel. [cite: 42]

#### 2.3 Governance by Design
[cite_start]Everything is versioned, audited, license-gated, policy-controlled, role-aware, and tenant-isolated. [cite: 44, 45, 46, 47, 48, 49, 50] [cite_start]Nothing meaningful happens without traceability. [cite: 51]

#### 2.4 Event-Driven & Scalable
* [cite_start]**PostgreSQL**: System of record. [cite: 56]
* [cite_start]**Redis**: Caching and fast reads. [cite: 57]
* [cite_start]**RabbitMQ**: Asynchronous messaging. [cite: 58]
* [cite_start]**CQRS-lite**: Read models. [cite: 58]
* [cite_start]**WebSockets**: Real-time updates. [cite: 59]
[cite_start]Screens must never depend on heavy synchronous calculations. [cite: 60]

#### 2.5 Security as a First-Class Concern
[cite_start]Includes multi-tenant isolation, row/field-level security, consent-based access, break-glass emergency overrides, and dual-control (four-eyes) protocols. [cite: 62, 63, 65]

---

### 3. Mental Model: ERP vs. Typical Apps
* **Normal Apps**: Logic is in services; [cite_start]UI validates; data is stored; logs exist. [cite: 71, 72, 73, 74, 75]
* [cite_start]**ERP Systems**: Every business object is governed by authorization objects, lifecycle states, number ranges, workflows, and audit trails. [cite: 76, 77, 78, 79, 80, 81, 82]
Data changes must be reversible, traceable, and explainable. [cite_start]Configuration is as important as code. [cite: 85, 86]

---

### 4. Architectural Layers
#### 4.1 Presentation Layer
* [cite_start]Angular web application and mobile/tablet apps. [cite: 90, 91]
* [cite_start]Resource-based internationalization and LLM assistant integration. [cite: 92, 94]
* [cite_start]The UI is modern, reactive, and transaction-aware. [cite: 95]

#### 4.2 Logic Layer (Java, LTS)
[cite_start]The kernel runtime includes engines for authorization, policies, rules, workflow states, and the job scheduler. [cite: 98, 99, 100, 101, 102, 103, 104]

#### 4.3 Data Layer (ANSI/SPARC 3-Schema Model)
[cite_start]Inspired by SAP’s Data Dictionary (DDIC), including domains, data elements, tables, views, and text tables for multilingual support. [cite: 114, 115, 116, 117, 118, 119, 120] [cite_start]All access flows through controlled repositories. [cite: 124]

---

### 5. Multi-Tenancy Model
Strategies supported:
1.  [cite_start]Separate schema per tenant. [cite: 127]
2.  [cite_start]Shared schema with tenant discriminator. [cite: 128]
3.  [cite_start]Hybrid models. [cite: 129]
[cite_start]Isolation is enforced at the service, authorization, and DB RLS layers. [cite: 130, 131, 132, 133]

---

### 6. Extensibility Model
* [cite_start]**Classes**: Core (internal), Client Z extensions, and Partner marketplace modules. [cite: 136, 137, 138, 139]
* [cite_start]**Governance**: Managed via namespace control, CI/CD gating (Gitea + Jenkins), and compatibility checks. [cite: 140, 141, 142, 146]

---

### 7. Rules & Workflow
[cite_start]Provides a declarative rules engine for billing, compensation, and clinical scoring (e.g., SOFA/APACHE). [cite: 149, 150, 156] [cite_start]Includes versioned rule activation and explainable execution traces. [cite: 151, 152]

---

### 8. Quality & Compliance
[cite_start]The Quality module integrates with operational events to support standards like NABH, JCI, and ISO. [cite: 158, 159, 161] [cite_start]It manages CAPA (Corrective and Preventive Actions) and produces compliance reports. [cite: 162, 163]

---

### 9. Internationalization
[cite_start]Supports multilingual master data, locale-aware UI bundles, and country-specific compliance packs. [cite: 166, 167, 168, 170]

---

### 10. Performance & 11. Assistant
* [cite_start]**Performance**: No heavy calculations on screen load; use of read models and batched server-side validation. [cite: 174, 175, 176]
* [cite_start]**Assistant**: Includes a RAG-based LLM assistant providing role-aware answers with full auditing. [cite: 187, 188, 189]

---

### 14. Summary for Developers
* [cite_start]Think in terms of **metadata** and **controlled lifecycles**. [cite: 207]
* [cite_start]Every action passes through **Licensing + Authorization + Policy + Audit**. [cite: 209]
* [cite_start]Data is not just stored—it is **governed**. [cite: 210]
* [cite_start]Extensions are welcome but **constrained**. [cite: 212]
* [cite_start]**You are not building a feature; you are building inside a regulated runtime.** [cite: 213, 214]
