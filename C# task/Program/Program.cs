using System;

namespace Program
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            Console.Write("Введите директорию файла (сепаратор = '/'): ");
            string path = Console.ReadLine().Trim();
            Console.Write("Введите имя файла с разрешением: ");
            string name = Console.ReadLine().Trim();
            
            var start = System.DateTime.Now;

            WordCounter wordCounter = new WordCounter(path, name);
            wordCounter.Count();
            
            var finish = System.DateTime.Now;
            Console.WriteLine($"Время выполнения: {(finish.TimeOfDay - start.TimeOfDay).Seconds} секунд");
        }
    }
}
