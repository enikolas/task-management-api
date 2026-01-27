# Tech Decisions

## About this document

This document records **non-trivial architectural decisions** made during the project.

It exists to capture *judgement calls*, not textbook knowledge.

It intentionally avoids documenting well-known patterns or frameworks, focusing instead on:

- trade-offs,
- deviations from strict best practices,
- conscious simplifications.

If a decision can be justified as
"this is how most well-designed systems already work",
it does not belong here.

---

## v0.0.1 - Project Setup (foundation)

### Partial Clean Architecture (pragmatic, not purist)

#### Decision

Adopt a **partial Clean Architecture**:

- strict separation between `domain` and `application`
- looser rules for infrastructure

#### Rationale

A fully purist Clean Architecture maximises theoretical replaceability
(databases, frameworks, transports),
but introduces significant delivery cost:

- additional abstractions,
- higher cognitive overhead,
- slower feature development.

In practice, most real systems:

- rarely replace their primary database,
- almost never migrate core frameworks,
- and when they do, the main difficulty is operational, not architectural.

The real goal is therefore not *perfect replaceability*,
but **protecting business rules while keeping delivery fast and clear**.

This middle ground keeps the critical parts of the system reusable,
without paying the full price of architectural purity.

As a secondary benefit, this also makes the system resilient to
inevitable infrastructure evolution:
major framework upgrades, library changes, and platform shifts
can be absorbed without impacting core business logic.

#### Trade-offs

Pros:

* business rules protected from framework concerns
* faster delivery with lower cognitive and structural overhead
* core logic resilient to infrastructure evolution

Cons:

* part of the system’s validation rules live in the infrastructure layer
  and are not automatically reusable across other interfaces
* domain relies more on usage discipline than on strict self-enforced invariants
* deviates from strict architectural orthodoxy, requiring conscious justification

---

## v0.1.0 - Authentication & Users (WIP)

### Validation outside domain

#### Decision

Most input validation is performed in the infrastructure layer using Spring Validation, 
rather than being enforced in the domain or application layers.

#### Rationale

A purist approach would place core validation rules inside the domain or application,
decoupled from any specific framework,
possibly duplicating them again at the presentation layer.

While this provides stronger isolation and portability,
it also introduces significant friction:

* additional boilerplate,
* duplicated validation logic,
* and slower iteration for simple API changes.

One of the primary reasons for adopting Spring Boot is precisely
to reduce incidental complexity and accelerate delivery.
Spring Validation offers a high return on investment:
declarative rules, well-tested behaviour,
and consistent error handling at minimal cost.

Reimplementing the same validation logic manually inside the core
would not be technically difficult,
but would primarily add mechanical work
with limited practical benefit at the current scale of the system.

#### Trade-offs

Pros:

* significantly faster delivery for API changes
* minimal boilerplate for common validation rules
* consistent and battle-tested error behaviour

Cons:

* validation rules are coupled to the web layer
* reduced portability across different interfaces (CLI, gRPC, batch)
* higher cost if the system needs to move away from Spring

#### Known risks

If the system evolves to support additional interfaces
or requires a major framework migration,
validation logic will need to be revisited and potentially reimplemented,
including full regression testing to preserve error semantics.

This risk is consciously accepted:
the immediate productivity gain is certain,
while the long-term migration cost remains hypothetical
and is deferred until a concrete need arises.

---

## Future versions (planned)

* v0.2.0 - Boards
* v0.3.0 - Lists
* v0.4.0 - Cards

Expected to follow the same architectural philosophy unless new constraints arise.

---

## Final note

This document intentionally records **why the system is not perfect**.

The goal is not theoretical purity, but **conscious engineering**:
knowing exactly what was sacrificed, and why.

> "Engineering is the art of choosing which problems you are willing to live with."
