package com.adiSuper.desk.dbAccessor;

import com.adiSuper.generated.core.Tables;
import com.adiSuper.generated.core.tables.pojos.Org;
import com.adiSuper.generated.core.tables.records.OrgRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;
import java.util.UUID;

@Repository
public class OrgDbAccessor extends BaseDbAccessor<OrgRecord, Org, Org, UUID> {

  private UUID defaultOrgId;

  @Value("${app.default.org.name}")
  public String  defaultOrgName;

  @Autowired
  private DSLContext db;


  @Override
  public Org copyOf(Optional<Org> optionalJp) {
    String errMessage = "Org not found";
    return optionalJp.orElseThrow(() -> new EntityNotFoundException(errMessage));
  }

  public UUID getDefaultOrgId(){
    if(defaultOrgId == null){
      defaultOrgId = db.select(Tables.ORG.ID).from(Tables.ORG).where(Tables.ORG.NAME.eq(defaultOrgName)).fetch().get(0).value1();
    }
    return defaultOrgId;
  }
}
