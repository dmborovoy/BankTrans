package com.mt940.functional.dao;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/dao-functional-test-context-dao.xml")
@Transactional(value = "bkvTransactionManager")
public abstract class AbstractBKVDaoBeanFunctionalTest {


}
