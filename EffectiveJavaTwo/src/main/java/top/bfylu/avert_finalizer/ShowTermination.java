package top.bfylu.avert_finalizer;

/**
 * 避免使用终结方法
 * @author bfy
 * @date 2018.3.16
 */
public class ShowTermination {



    public void fooOne() {

        //try-finally block guarantees execution of termination method
        //try-finally块保证执行终止方法
        Foo foo = new Foo();
        try {
            //Do what must be done with foo
            //TODO
        } finally {
            //foo.terminate();    //Explicit termination method(显式终止方法)
        }

    }

    private void terminate() {

    }


}
