CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
                                       conversation_id VARCHAR(36) NOT NULL,
                                       content LONGVARCHAR NOT NULL,
                                       type VARCHAR(10) NOT NULL,
                                       "timestamp" TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX ON SPRING_AI_CHAT_MEMORY(conversation_id, "timestamp" DESC);

-- H2 ne supporte pas toujours "ADD CONSTRAINT IF NOT EXISTS", il est préferable de l'éviter pour ne pas planter la redémarrage
-- On ignore l'alter table, ou on utilise une syntaxe specifique. Dans notre cas, la creation des tables avec IF NOT EXISTS suffit
-- ALTER TABLE SPRING_AI_CHAT_MEMORY ADD CONSTRAINT TYPE_CHECK CHECK (type IN ('USER', 'ASSISTANT', 'SYSTEM', 'TOOL'));