package dal;

public class MySQLDAOFactory extends AbstractDAOFactory {
    @Override
    public IWordDAO createWordDAO() {
        return new WordDAO(); 
    }
}
