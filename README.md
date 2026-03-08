# WorkFlow Approval System


##  Quick Run Instructions

The server will start on `http://localhost:8080`.

### 3. Database Access (H2 Console)
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:workflow`
- **User**: `sa`
- **Password**: (blank)

---


```sql
-- 1. Create Workflow Steps for 'LEAVE' requests
INSERT INTO APPROVAL_STEP (REQUEST_TYPE, STEP_ORDER, ROLE) VALUES ('LEAVE', 1, 'APPROVER');

-- 2. Create Users (You can also use /auth/register)
-- Password is 'pass123' (Plain text as per project configuration)
INSERT INTO USERS (EMAIL, PASSWORD, ROLE) VALUES ('requester@test.com', 'pass123', 'REQUESTER');
INSERT INTO USERS (EMAIL, PASSWORD, ROLE) VALUES ('approver@test.com', 'pass123', 'APPROVER');
INSERT INTO USERS (EMAIL, PASSWORD, ROLE) VALUES ('admin@test.com', 'pass123', 'ADMIN');
```

---


1.  **Self-Approval Prevention**: A user cannot approve or reject a request they created.
2.  **Role-Based Access**: Only a user holding the role assigned to the *current* step of a request can act on it.
3.  **Admin Override**: Users with the `ADMIN` role can manually Approve or Reject any request at any time, bypassing standard steps.
4.  **Immutability**: Once a request is `APPROVED` (all steps done) or `REJECTED`, no further actions can be taken.

### Key API Endpoints
- `POST /auth/register` & `/auth/login`: Authentication.
- `POST /requests/create?type=LEAVE`: Initialize a new workflow.
- `GET /requests/details/{id}`: View status and history.
- `POST /requests/{id}/approve`: Move to next step or complete workflow.
- `POST /requests/{id}/reject`: Terminate workflow.
- `GET /requests/history/{id}`: Audit trail of all actions.

---

##  Testing Flow
1. **Register** a Requester and an Approver.
2. **Login** as Requester to get a token.
3. **Create** a request (`POST /requests/create?type=LEAVE`).
4. **Login** as Approver to get a different token.
5. **Approve** the request using the Approver's token.
