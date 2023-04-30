using System;

namespace Program
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            Console.Write("Введите директорию файла (сепаратор = '/'): ");
            string path = Console.ReadLine()?.Trim();
            Console.Write("Введите имя файла с разрешением: ");
            string name = Console.ReadLine()?.Trim();
            
            WordCounter wordCounter = new WordCounter(path, name);
            wordCounter.Count();
            
            Console.WriteLine("Для завершения работы нажмите любую клавишу...");
            Console.ReadKey();
        }
    }
}
