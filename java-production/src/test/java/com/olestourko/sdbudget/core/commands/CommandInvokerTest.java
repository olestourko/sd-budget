package com.olestourko.sdbudget.core.commands;

import com.olestourko.sdbudget.core.commands.CommandInvoker;
import com.olestourko.sdbudget.core.commands.ICommand;
import com.olestourko.sdbudget.core.commands.ICommandCallback;
import com.olestourko.sdbudget.core.commands.mocks.MockCommand_EmptyImp;
import com.olestourko.sdbudget.core.commands.mocks.MockCommand_HistoryTestImp;
import com.olestourko.sdbudget.core.commands.mocks.StringWrapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author oles
 */
public class CommandInvokerTest {

    private CommandInvoker invoker;

    public CommandInvokerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        this.invoker = new CommandInvoker();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of invoke method, of class CommandInvoker.
     */
    @Test
    public void testInvoke() {
//        System.out.println("invoke");
//        ICommand command = null;
//        CommandInvoker instance = new CommandInvoker();
//        instance.invoke(command);
    }

    /**
     * Test of addListener method, of class CommandInvoker. Tests listener when
     * it is registered.
     */
    @Test
    public void testAddListener_1() {
        ICommand command = new MockCommand_EmptyImp();
        Handler handler = new Handler();
        invoker.addListener(command.getClass(), handler);
        invoker.invoke(command);
        assertEquals(true, handler.called);
    }

    /**
     * Test of addListener method, of class CommandInvoker. Tests listener when
     * it isn't registered.
     */
    @Test
    public void testAddListener_2() {
        ICommand command = new MockCommand_EmptyImp();
        Handler handler = new Handler();
        invoker.invoke(command);
        assertEquals(false, handler.called);
    }

    /**
     * Test of addListener method, using priority.
     */
    @Test
    public void testAddListener_usingPriority_1() {
        ICommand command = new MockCommand_EmptyImp();
        ObjectWrapper objectWrapper = new ObjectWrapper(null);
        ICommandCallback handler1 = new PriorityTestHandler(objectWrapper);
        ICommandCallback handler2 = new PriorityTestHandler(objectWrapper);
        ICommandCallback handler3 = new PriorityTestHandler(objectWrapper);
        invoker.addListener(command.getClass(), handler1, 2);
        invoker.addListener(command.getClass(), handler2, 1);
        invoker.addListener(command.getClass(), handler3, 0);
        invoker.invoke(command);
        // handler3 should be the last handler executed.
        assertEquals(handler3, objectWrapper.object);
    }

    /**
     * Test of addListener method, using priority.
     */
    @Test
    public void testAddListener_usingPriority_2() {
        ICommand command = new MockCommand_EmptyImp();
        ObjectWrapper objectWrapper = new ObjectWrapper(null);
        ICommandCallback handler1 = new PriorityTestHandler(objectWrapper);
        ICommandCallback handler2 = new PriorityTestHandler(objectWrapper);
        ICommandCallback handler3 = new PriorityTestHandler(objectWrapper);
        invoker.addListener(command.getClass(), handler3, 0);
        invoker.addListener(command.getClass(), handler2, 1);
        invoker.addListener(command.getClass(), handler1, 2);
        invoker.invoke(command);
        assertEquals(handler3, objectWrapper.object);
    }

    @Test
    /*
    * Test of undo method, of class CommandInvoker
     */
    public void testUndo() {
        StringWrapper stringWrapper = new StringWrapper("A");
        ICommand command1 = new MockCommand_HistoryTestImp(stringWrapper, "B");
        invoker.invoke(command1);
        ICommand command2 = new MockCommand_HistoryTestImp(stringWrapper, "C");
        invoker.invoke(command2);
        assertEquals("C", stringWrapper.getValue());
        invoker.undo();
        assertEquals("B", stringWrapper.getValue());
        invoker.undo();
        assertEquals("A", stringWrapper.getValue());
    }

    @Test
    /*
    * Test of canUndo method, of class CommandInvoker
     */
    public void testCanUndo() {
        ICommand command = new MockCommand_EmptyImp();
        assertEquals(false, invoker.canUndo());
        invoker.invoke(command);
        assertEquals(true, invoker.canUndo());
    }

    class Handler implements ICommandCallback {

        public boolean called = false;

        @Override
        public void handle(ICommand command) {
            called = true;
        }
    };

    class ObjectWrapper {

        public Object object;

        public ObjectWrapper(Object object) {
            this.object = object;
        }
    }

    class PriorityTestHandler implements ICommandCallback {

        private ObjectWrapper objectWrapper;

        public PriorityTestHandler(ObjectWrapper objectWrapper) {
            this.objectWrapper = objectWrapper;
        }

        @Override
        public void handle(ICommand command) {
            objectWrapper.object = this;
        }
    };

}
