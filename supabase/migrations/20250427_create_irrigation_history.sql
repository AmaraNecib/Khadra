CREATE TABLE irrigation_history (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    tree_id UUID NOT NULL REFERENCES trees(id) ON DELETE CASCADE,
    irrigation_date TIMESTAMP WITH TIME ZONE NOT NULL,
    notes TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW()),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT TIMEZONE('utc'::text, NOW())
);

CREATE INDEX idx_irrigation_history_tree_id ON irrigation_history(tree_id);
CREATE INDEX idx_irrigation_history_date ON irrigation_history(irrigation_date);

ALTER TABLE irrigation_history ENABLE ROW LEVEL SECURITY;

CREATE POLICY "Enable read access for all users" ON irrigation_history
    FOR SELECT USING (true);

CREATE POLICY "Enable insert for authenticated users" ON irrigation_history
    FOR INSERT WITH CHECK (true);

CREATE POLICY "Enable update for authenticated users" ON irrigation_history
    FOR UPDATE USING (true);

CREATE POLICY "Enable delete for authenticated users" ON irrigation_history
    FOR DELETE USING (true);
