//package source;
//
//
//import java.io.DataInput;
//import java.io.DataOutput;
//import java.io.IOException;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
//import org.apache.hadoop.io.Text;
//import org.apache.hadoop.io.Writable;
//import org.apache.hadoop.mapreduce.lib.db.DBWritable;
//
//public class LocationRecorder implements Writable, DBWritable {
//    String id;
//    int class_code;
//    int pbc_code;
//
//    @Override
//    public void readFields(DataInput in) throws IOException {
//        this.id = in.readString();
//    }
//
//    @Override
//    public void write(DataOutput out) throws IOException {
//        Text.writeString(out, this.id);
//    }
//
//    @Override
//    public void readFields(ResultSet result) throws SQLException {
//        this.id = result.getString(1);
//    }
//
//    @Override
//    public void write(PreparedStatement stmt) throws SQLException {
//        stmt.setString(1, this.id);
//    }
//
//    @Override
//    public String toString() {
//        return new String(this.id);
//    }
//}
