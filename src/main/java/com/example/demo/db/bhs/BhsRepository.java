package com.example.demo.db.bhs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BhsRepository {

    @Autowired
    @Qualifier("BhsJdbcTemplate")
    JdbcTemplate jdbcTemplate;

    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private SimpleJdbcInsert simpleJdbcInsert;

    public BsmDto findBsmById(String id) {

/*        List<BsmDto> dtoLst = jdbcTemplate.query("SELECT X.ID, X.BSM.GETCLOBVAL() AS BSM FROM PDS.BDM_BAGS X WHERE X.ID = ?",
                new Object[]{id},
                new CustomerRowMapper()
        );*/

        List<BsmDto> dtoLst = jdbcTemplate.query("SELECT X.ID, X.BSM.GETCLOBVAL() AS BSM FROM PDS.BDM_BAGS X " +
                        "WHERE X.BSM.GETCLOBVAL() IS NOT NULL AND ROWNUM = 1",
                //new Object[]{id},
                new CustomerRowMapper()
        );

        if (dtoLst.isEmpty()) {
            return null;
        } else if (dtoLst.size() == 1) { // list contains exactly 1 element
            return dtoLst.get(0);
        } else { // list contains more than 1 element
            // either return 1st element or throw an exception
            return dtoLst.get(0);
        }

    }

    class CustomerRowMapper implements RowMapper<BsmDto> {

        @Override
        public BsmDto mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final BsmDto customer = new BsmDto();

            customer.setId(rs.getInt("ID"));
            customer.setBsm(rs.getString("BSM"));

            return customer;
        }
    }

}
