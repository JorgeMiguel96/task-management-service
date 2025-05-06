CREATE TABLE IF NOT EXISTS tasks (
    id BIGINT NOT NULL,
    task_name VARCHAR(20) NOT NULL,
    description VARCHAR(255) NOT NULL,
    deadline VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    CONSTRAINT pk_task_id PRIMARY KEY(id),
    CONSTRAINT udx_task_id UNIQUE(task_name)
    );

CREATE SEQUENCE IF NOT EXISTS seq_tasks_id;
ALTER SEQUENCE IF EXISTS seq_tasks_id OWNED BY tasks.id;