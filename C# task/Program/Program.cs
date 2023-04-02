﻿using System;

namespace Program
{
    internal class Program
    {
        public static void Main(string[] args)
        {
            // Console.Write("Введите директорию файла (сепаратор = '/'): ");
            // string path = Console.ReadLine()?.Trim();
            // Console.Write("Введите имя файла с разрешением: ");
            // string name = Console.ReadLine()?.Trim();
            //
            var start = DateTime.Now;

            WordCounter wordCounter = new WordCounter(path:"C:/Users/user/Desktop", name:"s.txt");
            wordCounter.Count();
            
            var finish = DateTime.Now;
            Console.WriteLine($"Время выполнения: {(finish.TimeOfDay - start.TimeOfDay).Seconds} секунд");
            Console.ReadKey();
        }
    }
}
