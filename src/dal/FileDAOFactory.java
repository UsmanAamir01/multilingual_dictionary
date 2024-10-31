package dal;

public class FileDAOFactory extends AbstractDAOFactory {
    @Override
    public IWordDAO createWordDAO() {
        return new FileWordDAO("words.txt");
    }
}
