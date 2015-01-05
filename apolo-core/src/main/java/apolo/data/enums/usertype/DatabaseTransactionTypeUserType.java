package apolo.data.enums.usertype;

import apolo.data.enums.DatabaseTransactionType;
import br.com.insula.opes.hibernate.usertype.ImmutableUserType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;

public class DatabaseTransactionTypeUserType extends ImmutableUserType {
	private static final long serialVersionUID = 1L;

	public int[] sqlTypes() {
		return new int[] { Types.VARCHAR };
	}

	public Class<?> returnedClass() {
		return DatabaseTransactionType.class;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String value = rs.getString(names[0]);
		if (rs.wasNull()) {
			return null;
		}
		else {
			return DatabaseTransactionType.fromCode(value);
		}
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session)
			throws HibernateException, SQLException {
		if (value == null) {
			st.setNull(index, Types.VARCHAR);
		}
		else {
			st.setString(index, ((DatabaseTransactionType) value).getCode());
		}
	}

}
