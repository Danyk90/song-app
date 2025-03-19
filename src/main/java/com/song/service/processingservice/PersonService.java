/*
package com.song.service.processingservice;

import java.util.HashMap;
class Person{
    private Integer id;
    private String name;
    private String address;
    private String email;
    private String phone;
    private String dob;
    private String;
}
//Implement a cache in PersonService
class PersonService {
    PersonRepo repo;
    Map personCache= new HashMap<Integer,Person>();

    public Integer savePerson(Person input) {
       if(!personCache.contains(input.getId())) {
        Person person =   repo.savePerson(input);
        personCache.put(person.getId(),input);
        return person.getId();
        // return ID of the successfully saved object
    } else {

           //Person cachedPerson = personCache.get(input.)
          Person cachedPerson = personCache.getByKey(input.getId());
          if(!cachedPerson.equals(input)) {
              cachedPerson.put(cachedPerson.getId(),input);
          }
       }

        public Person getPersonById(Integer id) {
        // get object from DB by Id or null
    }
}
*/
