package com.adiSuper.desk.dbAccessor;


import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.UpdatableRecord;
import org.jooq.impl.DAOImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public class BaseDbAccessor<R extends UpdatableRecord<R>, P, JP, T> {

  @Autowired
  private DAOImpl<R, JP, T> dao;
  @Autowired
  private DSLContext db;

  public P fetchOne(Field<T> field, T value) {
    Optional<JP> jp = dao.fetchOptional(field, value);
    return this.copyOf(jp);
  }
  public boolean deleteOneById(T id){
    boolean exists = dao.existsById(id);
    if(!exists){
      this.copyOf(Optional.empty());
    }
    dao.deleteById(id);
    boolean deleted = !dao.existsById(id);
    return deleted;
  }

  public P copyOf(Optional<JP> optionalJp){
    // implement this method in subclass
    return null;
  }

}
