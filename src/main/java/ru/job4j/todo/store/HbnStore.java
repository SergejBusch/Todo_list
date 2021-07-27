package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.todo.model.Item;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

import org.hibernate.boot.registry.StandardServiceRegistry;
import ru.job4j.todo.model.UserItem;

public class HbnStore implements Store, AutoCloseable {

    private final StandardServiceRegistry registry;
    private final SessionFactory sf;

    private HbnStore() {
        registry = new StandardServiceRegistryBuilder()
                .configure().build();
        sf = new MetadataSources(registry)
                .buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final Store INST = new HbnStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Collection<Item> getAllTasks(int id) {
        System.out.println("it work");
        return this.tx(
                session -> session.createQuery("from Item i where i.userItem.id = :id")
                        .setParameter("id", id)
                        .list()
        );
    }

    @Override
    public Collection<Item> getUnfinishedTasks(int id) {
        System.out.println("it work too");
        return this.tx(
                session -> session.createQuery("from Item i where i.done = false and i.userItem.id = :id")
                        .setParameter("id", id)
                        .list()
        );
    }

    @Override
    public void saveOrUpdate(Item item) {
        this.txVoid(session -> session.saveOrUpdate(item));
    }

    @Override
    public UserItem getUserByEmail(String email) {
        var userItem =  this.tx(s -> s.createQuery(
                "from UserItem u where u.email = :email", UserItem.class)
                .setParameter("email", email)
                .getResultList().get(0));

        System.out.println(userItem);
        return userItem;
    }

    @Override
    public void saveOrUpdate(UserItem userItem) {
        this.txVoid(session -> session.saveOrUpdate(userItem));
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    private void txVoid(final Consumer<Session> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            command.accept(session);
            tx.commit();
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    @Override
    public void close() throws Exception {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}
