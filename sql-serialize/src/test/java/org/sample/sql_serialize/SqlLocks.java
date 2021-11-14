package org.sample.sql_serialize;

public class SqlLocks {
    public static final String[] sources = new String[]{
        "sys.dm_tran_locks"
    };

    public static class dm_tran_locks {
        public String resource_type;
        public String resource_subtype;
        public Number resource_database_id;
        public String resource_description;
        public Number resource_associated_entity_id;
        public Number resource_lock_partition;
        public Object request_mode;
        public String request_type;
        public String request_status;
        public Number request_reference_count;
        public Number request_lifetime;
        public Number request_session_id;
        public Number request_exec_context_id;
        public Object request_request_id;
        public String request_owner_type;
        public Object request_owner_id;
        public Object request_owner_guid;
        public Object request_owner_lockspace_id;
        public Object lock_owner_address;
    }
}
