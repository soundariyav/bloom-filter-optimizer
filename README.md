# DBShield - Bloom Filter Query Optimizer

Reduced database queries by 51% using probabilistic data structures

## Problem

My distributed chat application was hitting PostgreSQL for every username lookup—even when users didn't exist. 50% of queries were wasteful, each taking ~150ms.

## Solution

Built a Bloom Filter layer that definitively says "user doesn't exist" without touching the database.

Architecture:*
```
Request → Bloom Filter (0.001ms)
  ├─ Found 0 bit → Skip DB! (Definitely doesn't exist)
  └─ All bits 1 → Query PostgreSQL (Might exist - confirm)
```

Results

| Metric | Improvement |
|--------|-------------|
| DB Queries | **51% reduction** |
| Response Time | **51.47% faster** |
| Memory | 60KB (vs 4MB HashMap) |
| False Positives | 0.5% |

 What's a Bloom Filter?

- Bit array** storing 0s and 1s (not actual data)
- k hash functions** (~7) map elements to bit positions
- Key property: Can definitively say "NOT in database" (100% accurate)
- Trade-off: Small false positive rate (~0.5%)

HashMap vs Bloom Filter

| Aspect | HashMap | Bloom Filter |
|--------|---------|--------------|
| Memory (50K users) | 4MB | 60KB |
| Accuracy | 100% | 99.5% |
| Scalability | Limited | Millions of entries |

Tech Stack

Java 17 | Spring Boot 3.2 | PostgreSQL (AWS RDS) | Google Guava | Maven

Quick Start
```bash
# Clone
git clone https://github.com/YOUR-USERNAME/dbshield.git
cd dbshield

# Configure database in application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/your_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Run
mvn clean install
mvn spring-boot:run
```

API Endpoints
```bash
# Optimized check
GET /api/users/check-with-filter/{username}

# Baseline check
GET /api/users/check-without-filter/{username}

# Performance benchmark
POST /api/demo/bulk-test?count=1000
```

 Test It
```bash
# Non-existent user (database skipped!)
curl http://localhost:8080/api/users/check-with-filter/nonexistent_user

# Response:
# {
#   "exists": false,
#   "durationMs": 0.001,
#   "databaseQueried": false  ← Database skipped!
# }
```

## Key Takeaway

The best optimization is avoiding work entirely. Bloom Filters let you definitively say "NO" without hitting the database.

## Author
Soundariya Vijayakumar

Star this repo if you found it helpful!
