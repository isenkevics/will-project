//my first C# program

// Hello Word
//using System;

using System.Security;
using System.Runtime.InteropServices;
using System.Diagnostics.CodeAnalysis;


static class Hello1 
{ 
    //[STAThread] 
    public static void Main(string[] args)
    {
        bool consoleMode = false; // Boolean.Parse(args[0]); //true; // ;
 
        if (consoleMode) { 
            System.Console.WriteLine("consolemode started");
            System.Diagnostics.Debug.WriteLine("consolemode started");
			System.Diagnostics.Debug.WriteLine("consolemode started - 2 time");
        } else {
            System.Diagnostics.Debug.WriteLine("Hello, World! - inicio");
			System.Diagnostics.Debug.WriteLine("Hello, World! - inicio - 1");
            System.Console.WriteLine("Hello, World!");
            System.Console.WriteLine("Hello, World!");
            System.Console.WriteLine("Hello, World!");
            System.Console.WriteLine("Hello, World!");
            System.Diagnostics.Debug.WriteLine("Hello, World! - termino");
			System.Diagnostics.Debug.WriteLine("Hello, World! - termino - 1");
        }
        System.Console.WriteLine("consolemode finished");
        System.Diagnostics.Debug.WriteLine("consolemode finished");
		System.Diagnostics.Debug.WriteLine("consolemode finished - 2 time");
    }
}



