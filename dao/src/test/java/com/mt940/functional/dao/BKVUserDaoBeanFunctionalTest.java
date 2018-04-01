package com.mt940.functional.dao;

import com.mt940.dao.jpa.BKVUserDao;
import com.mt940.dao.repository.BkvInstanceRepository;
import com.mt940.domain.enums.BKVRoles;
import com.mt940.domain.enums.Instance;
import com.mt940.domain.permission.BKVInstance;
import com.mt940.domain.permission.BKVRole;
import com.mt940.domain.permission.BKVUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.persistence.PersistenceException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

public class BKVUserDaoBeanFunctionalTest extends AbstractBKVDaoBeanFunctionalTest {

    @Autowired
    @Qualifier(value = "bkvUserDaoImpl")
    BKVUserDao dao;
    @Qualifier("bkvInstanceRepository")
    @Autowired
    BkvInstanceRepository instanceRepository;

    @Test
    public void testDelete() throws Exception {
        BKVUser byId = dao.findById(1L);
        l.info("{}", byId);
        assertNotNull(byId);
        dao.deleteById(1L);
//        dao.flush();
//        dao.clear();
        byId = dao.findById(1L);
        assertNull(byId);
    }

    @Test
    public void testRoles() throws Exception {
        BKVUser user = new BKVUser("name", "password", "description", false);
        user.setRoles(BKVRoles.API_ADMIN, BKVRoles.UI_USER);
        dao.save(user);
        dao.flush();
        dao.clear();
        List<BKVUser> all = dao.findAll();
        assertNotNull(all);
        l.info("all: {}", all);
        BKVUser savedUser = dao.findById(user.getId());
        assertEquals(new HashSet<BKVRoles>(Arrays.asList(BKVRoles.API_ADMIN, BKVRoles.UI_USER)), savedUser.getRoles());
        dao.delete(savedUser);
        dao.flush();
        dao.clear();
        all = dao.findAll();
        assertNotNull(all);
        l.info("all: {}", all);
    }

    private BKVInstance getEntity(Instance instance) {
        return instanceRepository.findOne(instance.getCode());
    }

    @Test
    public void testInstances() throws Exception {
        BKVUser user = new BKVUser("name", "password", "description", false);
        BKVInstance instance1 = getEntity(Instance.RUSSIA);
        BKVInstance instance2 = getEntity(Instance.EUROPE);
        user.setInstances(new HashSet(Arrays.asList(instance1, instance2)));
        dao.save(user);
        dao.flush();
        dao.clear();
        List<BKVUser> all = dao.findAll();
        assertNotNull(all);
        l.info("all: {}", all);
        dao.delete(dao.findById(user.getId()));
        dao.flush();
        dao.clear();
        all = dao.findAll();
        assertNotNull(all);
        l.info("all: {}", all);
    }

    @Test(expected = PersistenceException.class)
    public void testWrongRole() throws Exception {
        BKVUser user = new BKVUser("name", "password", "description", false);
        BKVRole role1 = new BKVRole();
        role1.setId(666);
        role1.setDescription("Wrong role");
        user.setRoles(new HashSet(Arrays.asList(role1)));
        dao.save(user);
        dao.flush();
        dao.clear();
    }

    @Test(expected = PersistenceException.class)
    public void testWrongInstance() throws Exception {
        BKVUser user = new BKVUser("name", "password", "description", false);
        BKVInstance instance1 = new BKVInstance();
        instance1.setId(666);
        instance1.setCode("Wrong code");
        instance1.setValue("Wrong value");
        user.setInstances(new HashSet(Arrays.asList(instance1)));
        dao.save(user);
        dao.flush();
        dao.clear();
    }

    @Test
    public void testFindUser() throws Exception {
        l.debug("+++++++++++++++++++++++++");
        BKVUser nxall = dao.findUser("dxall");
        assertNotNull(nxall);
        assertEquals("dxall", nxall.getLogin());
        assertEquals("123", nxall.getPassword());
        assertTrue(nxall.isSuperAdmin());
        assertEquals(0, nxall.getRoles().size());
        assertEquals(0, nxall.getInstances().size());

        BKVUser admin = dao.findUser("admin");
        assertNotNull(admin);
        assertEquals("admin", admin.getLogin());
//        assertEquals("$2a$10$VLaQ4DiWUtMEIPr2GMKG0OF4kTgrVftTwVjHBHGNeMGwXyfh/i4h2", admin.getPassword());
        assertTrue(!admin.isSuperAdmin());
        assertEquals(2, admin.getRoles().size());
        assertEquals(3, admin.getInstances().size());

        BKVUser wrong = dao.findUser("wrong");
        assertNull(wrong);

        BKVUser nll = dao.findUser(null);
        assertNull(nll);

    }
}