package fileimport;

public interface IValidateFunction extends IFunction{
	public void validate(Object obj, IImportRequest request) throws Exception;
}
